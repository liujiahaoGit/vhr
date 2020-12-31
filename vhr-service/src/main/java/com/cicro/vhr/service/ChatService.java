package com.cicro.vhr.service;

import com.cicro.vhr.mapper.HrMapper;
import com.cicro.vhr.model.Hr;
import com.cicro.vhr.util.HrUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {


    @Autowired
    private HrMapper hrMapper;

    public List<Hr> getAllHrIncludeCurrent() {
        return hrMapper.getAllHrIncludeCurrent(HrUtils.getCurrentUser().getId()) ;
    }
}
