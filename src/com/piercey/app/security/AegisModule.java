package com.piercey.app.security;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.piercey.app.ApplicationLogger;

public class AegisModule extends AbstractModule
{
	private static final ApplicationLogger logger = new ApplicationLogger(AegisModule.class);
	
	public void configure()
	{
		logger.executionTrace();
		
		bindInterceptor(
				Matchers.annotatedWith(RequiresAuthentication.class),
				Matchers.any(),
				new AegisInterceptor());
	}
}
