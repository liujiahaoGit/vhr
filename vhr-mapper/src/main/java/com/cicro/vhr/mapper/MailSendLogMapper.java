package com.cicro.vhr.mapper;

import com.cicro.vhr.model.MailSendLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MailSendLogMapper {
    Integer updateStatus(@Param("msgId") String msgId,@Param("status") Integer status);

    Integer insertMSL(MailSendLog log);

    List<MailSendLog> getEmpByStatus();

    Integer updateCount(@Param("msgId") String msgId, @Param("date")Date date);
}
