package com.myproject.qs.qs;

import com.myproject.qs.qs.Filter.SessionFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@ServletComponentScan
@SpringBootApplication
public class QsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QsApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean sessionFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SessionFilter sessionFilter = new SessionFilter();
        registrationBean.setFilter(sessionFilter);
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
