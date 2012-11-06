package com.piercey.app.framework;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class ApplicationFilter extends GuiceFilter
{
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationFilter.class);
	private static Injector applicationInjector;
	private static Injector securityInjector;

	public static Injector getApplicationInjector()
	{
		logger.executionTrace();
		return applicationInjector;
	}

	public static Injector getSecurityInjector()
	{
		logger.executionTrace();
		return securityInjector;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		logger.executionTrace();
		super.doFilter(request, response, chain);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		logger.executionTrace();

		if (applicationInjector != null)
			throw new ServletException("application injector already created");

		if (securityInjector != null)
			throw new ServletException("security injector already created");

		 final Realm realm = new ApplicationSecurityRealm();
		 final SecurityManager securityManager = new DefaultSecurityManager(realm);
		 SecurityUtils.setSecurityManager(securityManager);
		
		applicationInjector = Guice.createInjector(new ApplicationModule());
		securityInjector = Guice.createInjector(new ApplicationSecurityModule());

		super.init(filterConfig);
	}
}
