package com.example.demo.filter;

import com.example.demo.domain.MobileUser;
import lombok.Data;

public class UserContext {
    private static ThreadLocal<MobileUser> threadLocal=new ThreadLocal();

    public static MobileUser getThreadLocal() {
        return threadLocal.get();
    }

    public static void setThreadLocal(MobileUser mobileUser) {
        threadLocal.set(mobileUser);
    }
}
