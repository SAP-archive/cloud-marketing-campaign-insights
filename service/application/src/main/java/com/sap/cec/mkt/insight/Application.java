package com.sap.cec.mkt.insight;

import javax.servlet.ServletContext;

import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.WebApplicationContext;

import com.sap.cec.mkt.insight.servlet.MarketingInsightODataServlet;

@SpringBootApplication
@ServletComponentScan(basePackages = { "com.sap.cec.mkt.insight" })
@ComponentScan(basePackages = { "com.sap.cec.mkt.insight" })
public class Application extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
	return application.sources(Application.class);
    }

    public static void main(final String[] args) {
	SpringApplication.run(Application.class, args);
    }

    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
	WebApplicationContext context = super.createRootApplicationContext(servletContext);
	return context;
    }

    @Bean
    public ServletContextInitializer initializer() {
	return servletContext -> {
	    servletContext.setInitParameter("package", "com.sap.cec.mkt.insight");
	};
    }

    @Bean
    public ServletRegistrationBean<MarketingInsightODataServlet> odataServletRegistrationBean() {
	ServletRegistrationBean<MarketingInsightODataServlet> registration = new ServletRegistrationBean<MarketingInsightODataServlet>(
		new MarketingInsightODataServlet(), "/odata/v4/*");
	return registration;
    }

    @Bean
    public ODataApplication executorListener() {
	return new ODataApplication();
    }
}
