package com.springboot.app.utils;

import com.springboot.app.exception.BusinessEx;
import com.springboot.app.security.user_principle.UserPrincipal;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class ContextUtils {
    public String getCurrentUser() {
        try {
            var context = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return  context.getUsername();
        } catch (Exception e) {
            throw new BusinessEx();
        }
    }

    public UserPrincipal getPrincipal() {
        try {
            return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BusinessEx();
        }
    }

    public Long getCurrentUserId() {
        try {
            var context = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return  context.getId();
        } catch (Exception e) {
            throw new BusinessEx();
        }
    }
}
