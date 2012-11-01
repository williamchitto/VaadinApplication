package com.piercey.app;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;

public class ApplicationFilter extends GuiceFilter
{
	private static Injector injector;

	public static Injector getInjector()
	{
		return injector;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException
	{
		//String uri = ((HttpServletRequest) request).getRequestURI();
		super.doFilter(request, response, chain);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		if (injector != null)
		{
			throw new ServletException("Injector already created?!");
		}
		
		injector = Guice.createInjector(new ApplicationModule());
		filterConfig.getServletContext().log(
				"Created injector with " + injector.getAllBindings().size() + " bindings.");

		super.init(filterConfig);
	}
}
