package com.piercey.app;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.piercey.app.security.SecurityModule;

public class ApplicationFilter extends GuiceFilter
{
	private static Injector applicationInjector;
	private static Injector securityInjector;

	public static Injector getApplicationInjector()
	{
		return applicationInjector;
	}

	public static Injector getSecurityInjector()
	{
		return securityInjector;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		super.doFilter(request, response, chain);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		if (applicationInjector != null)
			throw new ServletException("application injector already created");
		
		if (securityInjector != null)
			throw new ServletException("security injector already created");
		
		final Realm realm = new IniRealm();
		final SecurityManager securityManager = new DefaultSecurityManager(realm);
		SecurityUtils.setSecurityManager(securityManager);

		applicationInjector = Guice.createInjector(new ApplicationModule());
		securityInjector = Guice.createInjector(new SecurityModule());

		super.init(filterConfig);
	}
}
