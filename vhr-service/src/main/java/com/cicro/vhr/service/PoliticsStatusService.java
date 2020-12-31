package com.cicro.vhr.service;

import com.cicro.vhr.mapper.PoliticsstatusMapper;
import com.cicro.vhr.model.Politicsstatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoliticsStatusService {

    @Autowired
    private PoliticsstatusMapper politicsstatusMapper;

    public List<Politicsstatus> getAllPoliticsStatus() {
        return politicsstatusMapper.getAllPoliticsStatus();

    }
}
