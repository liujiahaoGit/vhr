package com.cicro.vhr.service;

import com.cicro.vhr.mapper.NationMapper;
import com.cicro.vhr.model.Nation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationService {

    @Autowired
    private NationMapper nationMapper;

    public List<Nation> getAllNation() {
        return nationMapper.getAllNation();

    }
}
