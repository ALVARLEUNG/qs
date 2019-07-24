package com.myproject.qs.qs.listener;

import com.myproject.qs.qs.common.utils.SessionInfoUtil;
import com.myproject.qs.qs.domainobject.Auditor;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    //创建session时触发
    public void attributeAdded(HttpSessionBindingEvent event) {
        if ("userInfo".equals(event.getName())) {
            SessionInfoUtil.setLocal((Auditor) event.getValue());
        }
    }

    @Override
    //销毁session时触发
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if ("userInfo".equals(event.getName())) {
            SessionInfoUtil.removeLocal();
        }
    }

    @Override
    //替换session时触发
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if ("userInfo".equals(event.getName())) {
            SessionInfoUtil.setLocal((Auditor) event.getValue());
        }
    }
}
