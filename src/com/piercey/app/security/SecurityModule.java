package com.piercey.app.security;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class SecurityModule extends AbstractModule
{
	public void configure()
	{
		bindInterceptor(Matchers.any(), Matchers.any(), new SecurityInterceptor());
		
//		bindInterceptor(
//				Matchers.annotatedWith(RequiresAuthentication.class)
//					.or(Matchers.annotatedWith(RequiresUser.class))
//					.or(Matchers.annotatedWith(RequiresRoles.class))
//					.or(Matchers.annotatedWith(RequiresPermissions.class))
//					.or(Matchers.annotatedWith(RequiresGuest.class)),
//				Matchers.annotatedWith(RequiresAuthentication.class)
//					.or(Matchers.annotatedWith(RequiresUser.class))
//					.or(Matchers.annotatedWith(RequiresRoles.class))
//					.or(Matchers.annotatedWith(RequiresPermissions.class))
//					.or(Matchers.annotatedWith(RequiresGuest.class)),
//				new SecurityInterceptor());
	}
}
