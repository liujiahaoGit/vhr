package com.cicro.vhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private Integer rid;

    private String name;

    private String nameZh;

    private List<Menu> menus;
}
