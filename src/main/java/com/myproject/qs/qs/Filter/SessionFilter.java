package com.myproject.qs.qs.Filter;

import com.myproject.qs.qs.domainobject.Auditor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        Auditor auditor= (Auditor) request.getSession().getAttribute("userInfo");
        if (auditor != null) {
            //先销毁在添加否则触发不了监听器中的attributeAdded
            request.getSession().removeAttribute("userInfo");
            //重新设值session
            request.getSession().setAttribute("userInfo", auditor);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}

