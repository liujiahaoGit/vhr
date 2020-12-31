package com.cicro.vhr.service;

import com.cicro.vhr.mapper.MailSendLogMapper;
import com.cicro.vhr.model.MailSendLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MailSendLogService {

    @Autowired
    private MailSendLogMapper sendLogMapper;

    public Integer updateStatus(String msgId, Integer i) {
        return sendLogMapper.updateStatus(msgId, i);

    }

    public Integer insert(MailSendLog log) {
        return sendLogMapper.insertMSL(log);
    }

    public List<MailSendLog> getEmpByStatus() {
        return sendLogMapper.getEmpByStatus();
    }

    public Integer updateCount(String msgId, Date date) {
        return sendLogMapper.updateCount(msgId,date);
    }
}
