package com.cicro.vhr.service;

import com.cicro.vhr.entity.MailConstants;
import com.cicro.vhr.entity.ResultPage;
import com.cicro.vhr.mapper.DepartmentMapper;
import com.cicro.vhr.mapper.EmployeeMapper;
import com.cicro.vhr.mapper.JobLevelMapper;
import com.cicro.vhr.mapper.NationMapper;
import com.cicro.vhr.mapper.PoliticsstatusMapper;
import com.cicro.vhr.mapper.PositionMapper;
import com.cicro.vhr.model.Department;
import com.cicro.vhr.model.Employee;
import com.cicro.vhr.model.JobLevel;
import com.cicro.vhr.model.MailSendLog;
import com.cicro.vhr.model.Nation;
import com.cicro.vhr.model.Politicsstatus;
import com.cicro.vhr.model.Position;
import com.cicro.vhr.util.POIUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@SuppressWarnings("all")
public class EmpService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private NationMapper nationMapper;

    @Autowired
    private PoliticsstatusMapper politicsstatusMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private JobLevelMapper jobLevelMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailSendLogService sendLogService;

    public ResultPage getAllEmployee(Integer page, Integer size, Employee employee, Date[] beginDate) {
        PageHelper.startPage(page, size);
        List<Employee> employees = employeeMapper.getAllEmployee(employee, beginDate);
        PageInfo pageInfo = new PageInfo(employees);
        ResultPage resultPage = ResultPage.builder()
            .list(pageInfo.getList())
            .pageTotal(pageInfo.getPages())
            .pageSize(pageInfo.getPageSize())
            .currPage(pageInfo.getPageNum())
            .totalCount(pageInfo.getTotal())
            .build();
        return resultPage;
    }

    //业务说明:当添加员工成功后给MQ发送消息,监听端给员工发送入职邮件
    public Integer insertEmp(Employee employee) {

        int i = employeeMapper.insertSelective(employee);
        if (i == 1) {
            Employee employee1 = employeeMapper.getEmployeeById(employee.getId());
            log.info("发送内容{}",employee1.toString());

            //发送消息前记录消息日志表
            String msgId = UUID.randomUUID().toString();
            MailSendLog log = new MailSendLog();
            log.setEmpId(employee1.getId());
            log.setMsgId(msgId);
            log.setStatus(MailConstants.DELIVERING);
            log.setCount(0);
            log.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            log.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            log.setCreateTime(new Date());
            //记录重试时间(假设每10s扫描一次消息表,但有可能存在刚刚存进表 生产端还没来得及发送就被定时任务扫到 因此这里定时任务扫描规则为当前时间大于重试时间时判断为发送失败)
            log.setTryTime(new Date(System.currentTimeMillis()+1000*60*MailConstants.MSG_TIMEOUT));
            sendLogService.insert(log);
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,employee1,new CorrelationData(msgId));
        }
        return i;
    }

    public String getWorkId() {
        String workId = employeeMapper.getWorkId();
        log.info("工号自增ID为:" + String.format("%08d", Integer.parseInt(workId) + 1));
        return String.format("%08d", Integer.parseInt(workId) + 1);

    }

    public Integer deleteEmpById(Integer id) {
        return employeeMapper.deleteByPrimaryKey(id);
    }

    public Integer updateEmpById(Employee e) {
        return employeeMapper.updateByPrimaryKeySelective(e);
    }

    public ResponseEntity<byte[]> export() throws IOException {
        //查出所有需要导出的数据
        List<Employee> list = (List<Employee>) getAllEmployee(1, Integer.MAX_VALUE, null, null).getList();

        for (Employee employee : list) {
            employee.setNationName(employee.getNation().getName());
            employee.setPos(employee.getPosition().getName());
            employee.setJobLevelName(employee.getJobLevel().getName());
            employee.setDepartmentName(employee.getDepartment().getName());
            employee.setPolitic(employee.getPoliticsstatus().getName());
        }
        return POIUtils.export(list, Employee.class, "员工信息表", "员工信息", true, null);

    }

    /*
     * @methodName: importEmp
     * @description Excel导入
     * @param: file 前端的导入的文件名称要和后一致
     * @return:
     * @createdAt 11:44 2020/8/5 0005
     * @version 1.0.0
     **/
    public Boolean importEmp(MultipartFile file) {

        List<Employee> employees = POIUtils.importExcel(file, 1, 1, Employee.class);

        List<Nation> allNation = nationMapper.getAllNation();

        List<Politicsstatus> allPoliticsStatus = politicsstatusMapper.getAllPoliticsStatus();

        List<Department> departments = departmentMapper.queryAllDeptName();

        List<JobLevel> jobLevels = jobLevelMapper.queryJobs();

        List<Position> positions = positionMapper.queryPosition();

        LinkedList<Employee> newList = new LinkedList<>();

        //这里因为相关字段库里存的是关联ID 而要从本地导入的表里存的是相关的名称 所以要在这里做相应处理(将Excel表里名称转换为对应ID)
        for (Employee employee : employees) {
            employee.setId(null); //数据库主键自增
            for (Nation nation : allNation) {
                if (nation.getName().equals(employee.getNationName())) {
                    employee.setNationId(nation.getId());
                    break;
                }
            }

            for (Politicsstatus politicsStatus : allPoliticsStatus) {
                if (politicsStatus.getName().equals(employee.getPolitic())) {
                    employee.setPoliticId(politicsStatus.getId());
                    break;
                }
            }

            for (Department department : departments) {
                if (department.getName().equals(employee.getDepartmentName())) {
                    employee.setDepartmentId(department.getId());
                    break;
                }
            }

            for (JobLevel jobLevel : jobLevels) {
                if (jobLevel.getName().equals(employee.getJobLevelName())) {
                    employee.setJobLevelId(jobLevel.getId());
                    break;
                }
            }

            for (Position position : positions) {
                if (position.getName().equals(employee.getPos())) {
                    employee.setPosId(position.getId());
                    break;
                }
            }
            newList.add(employee);
        }
        int i = employeeMapper.batchInsert(newList); //批量插入数据库

        if (i == newList.size()) {
            return true;
        }
        return false;
    }

    public Employee getEmpById(Integer empId) {
        return employeeMapper.selectByPrimaryKey(empId);
    }
}
