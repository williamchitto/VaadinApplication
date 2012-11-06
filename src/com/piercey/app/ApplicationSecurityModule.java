package com.piercey.app;

import java.lang.reflect.Method;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.guice.ShiroModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

public class ApplicationSecurityModule extends ShiroModule
{
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationSecurityModule.class);

	@Override
	protected void configureShiro()
	{
		logger.executionTrace();

		try
		{
			final Matcher<? super Class<?>> classMatcher =
					Matchers.annotatedWith(RequiresAuthentication.class);

			final Matcher<? super Method> methodMatcher =
					Matchers.any();

			bindRealm().toConstructor(ApplicationSecurityRealm.class.getConstructor());
			bindInterceptor(classMatcher, methodMatcher, new ApplicationSecurityInterceptor());
		}
		catch (NoSuchMethodException e)
		{
			addError(e);
		}
	}
}
