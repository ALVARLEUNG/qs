package com.myproject.qs.qs.common.utils;

import com.myproject.qs.qs.domainobject.Auditor;

/*1、ThreadLocal.get: 获取ThreadLocal中当前线程共享变量的值。

2、ThreadLocal.set: 设置ThreadLocal中当前线程共享变量的值。

3、ThreadLocal.remove: 移除ThreadLocal中当前线程共享变量的值
*/

public class SessionInfoUtil {

   /*  本地线程变量ThreadLocal
     用于保存某个线程共享变量：对于同一个static ThreadLocal，
     不同线程只能从中get，set，remove自己的变量，而不会影响其他线程的变量。*/
    private static ThreadLocal<Auditor> local = new ThreadLocal<>();

    public static void setLocal (Auditor auditor) {
        local.set(auditor);
    }

    public static Auditor getLocal () {
        return local.get();
    }

    public static void removeLocal () {
        local.remove();
    }
}
