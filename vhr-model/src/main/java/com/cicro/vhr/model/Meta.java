package com.cicro.vhr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

    private Boolean keepAlive;

    private Boolean requireAuth;
}
