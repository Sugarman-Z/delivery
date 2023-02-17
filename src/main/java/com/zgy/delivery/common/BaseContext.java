package com.zgy.delivery.common;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * set员工id
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * get员工id
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
