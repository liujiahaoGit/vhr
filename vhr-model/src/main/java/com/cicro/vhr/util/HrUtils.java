package com.cicro.vhr.util;

import com.cicro.vhr.model.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

public class HrUtils {

    public static Hr getCurrentUser() {

        return ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
