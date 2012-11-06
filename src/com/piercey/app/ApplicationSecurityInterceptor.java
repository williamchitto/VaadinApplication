package com.piercey.app;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import com.piercey.app.views.ApplicationView;
import com.piercey.app.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class ApplicationSecurityInterceptor implements MethodInterceptor
{
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationSecurityInterceptor.class);

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		//logger.executionTrace();
		
		final Class<?> classType = invocation.getMethod().getDeclaringClass();
		final Method method = invocation.getMethod();

		final String viewName = classType.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		long startTime = System.nanoTime();

		try
		{
			final String principal = ApplicationSecurity.getPrincipal();
			CheckRequiresAuthentication(classType, method, principal);
			CheckRequiresUser(classType, method, principal);
			CheckRequiresGuest(classType, method, principal);
			CheckRequiresRoles(classType, method, principal);
			CheckRequiresPermissions(classType, method, principal);

			return invocation.proceed();
		}
		catch (AuthorizationException e)
		{
			final Navigator navigator = UI.getCurrent().getNavigator();
			Notification.show("Authentication Violation", Type.TRAY_NOTIFICATION);
			navigator.navigateTo(LoginView.NAME);
		}
		catch (AuthenticationException e)
		{
			final Navigator navigator = UI.getCurrent().getNavigator();
			Notification.show("Authorization Violation", Type.TRAY_NOTIFICATION);
			navigator.navigateTo(ApplicationView.NAME);
		}
		finally
		{
			final double duration = (System.nanoTime() - startTime) / 1000000.0;
			final String message = String.format("%s took %.3f ms to examine %s",
					getClass().getSimpleName(), duration, methodName);
			logger.debug(message);
		}
		
		return null;
	}

	/**
	 * Check for @RequiresAuthentication
	 * 
	 * This annotation requires the current subject to have been authenticated during their current session for the
	 * annotated class/instance/method to be accessed or invoked. This is more restrictive than the RequiresUser
	 * annotation.
	 * 
	 * This annotation basically ensures that subject.isAuthenticated() === true
	 */
	private void CheckRequiresAuthentication(Class<?> runtimeClass, Method method, String userName) throws Throwable
	{
		final String viewName = runtimeClass.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		boolean requiresAuthentication = false;

		try
		{
			RequiresAuthentication annotation = runtimeClass.getAnnotation(RequiresAuthentication.class);
			requiresAuthentication = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresAuthentication = false;
		}

		if (requiresAuthentication && !ApplicationSecurity.isAuthenticated())
		{
			final String message = String.format("Authentication failed for %s on %s", userName, viewName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}

		try
		{
			RequiresAuthentication annotation = method.getAnnotation(RequiresAuthentication.class);
			requiresAuthentication = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresAuthentication = false;
		}

		if (requiresAuthentication && !ApplicationSecurity.isAuthenticated())
		{
			final String message = String.format("Authentication failed for %s on %s", userName, methodName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}
	}

	/**
	 * Check @RequiresUser at the class level.
	 * 
	 * This annotation requires the current subject to be an application user for the annotated class/instance/method to
	 * be accessed or invoked. This is less restrictive than the RequiresAuthentication annotation. Shiro defines a
	 * "user" as a Subject that is either "remembered" or authenticated:
	 * 
	 * An authenticated user is a Subject that has successfully logged in (proven their identity) during their current
	 * session. A remembered user is any Subject that has proven their identity at least once, although not necessarily
	 * during their current session, and asked the system to remember them.
	 */
	private void CheckRequiresUser(Class<?> runtimeClass, Method method, String userName) throws Throwable
	{
		final String viewName = runtimeClass.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		boolean requiresUser = false;

		try
		{
			RequiresUser annotation = runtimeClass.getAnnotation(RequiresUser.class);
			requiresUser = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresUser = false;
		}

		if (requiresUser && !(ApplicationSecurity.isRemembered() || ApplicationSecurity.isAuthenticated()))
		{
			final String message = String.format("Authentication failed for %s on %s", userName, viewName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}

		try
		{
			RequiresUser annotation = method.getAnnotation(RequiresUser.class);
			requiresUser = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresUser = false;
		}

		if (requiresUser && !(ApplicationSecurity.isRemembered() || ApplicationSecurity.isAuthenticated()))
		{
			final String message = String.format("Authentication failed for %s on %s", userName, methodName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}
	}

	/**
	 * Check @RequiresGuest at the class level.
	 * 
	 * This annotation requires the current Subject to be a "guest", that is, they are not authenticated or remembered
	 * from a previous session for the annotated class/instance/method to be accessed or invoked.
	 * 
	 * This annotation is the logical inverse of the RequiresUser annotation. That is, RequiresUser == !RequiresGuest,
	 * or more accurately,
	 * 
	 * RequiresGuest === subject.getPrincipal() == null.
	 */
	private void CheckRequiresGuest(Class<?> runtimeClass, Method method, String userName) throws Throwable
	{
		final String viewName = runtimeClass.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		boolean requiresGuest = false;

		try
		{
			RequiresGuest annotation = runtimeClass.getAnnotation(RequiresGuest.class);
			requiresGuest = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresGuest = false;
		}

		if (requiresGuest && !ApplicationSecurity.isGuest())
		{
			final String message = String.format("Guest role is required for %s on %s", userName, viewName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}

		try
		{
			RequiresGuest annotation = method.getAnnotation(RequiresGuest.class);
			requiresGuest = (annotation != null);
		}
		catch (NullPointerException e)
		{
			requiresGuest = false;
		}

		if (requiresGuest && !ApplicationSecurity.isGuest())
		{
			final String message = String.format("Guest role is required for %s on %s", userName, methodName);
			logger.warn(message);
			throw new AuthenticationException(message);
		}
	}

	/**
	 * Check @RequiresRoles at the class level.
	 * 
	 * This annotation requires the currently executing Subject to have all of the specified roles. If they do not have
	 * the role(s), the method will not be executed and an AuthorizationException is thrown.
	 */
	private void CheckRequiresRoles(Class<?> runtimeClass, Method method, String userName) throws Throwable
	{
		final String viewName = runtimeClass.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		boolean requiresRoles = false;
		List<String> requiredRoles = null;

		try
		{
			RequiresRoles annotation = runtimeClass.getAnnotation(RequiresRoles.class);
			requiresRoles = (annotation != null);
			requiredRoles = Arrays.asList(annotation.value());
		}
		catch (NullPointerException e)
		{
			requiresRoles = false;
		}

		if (requiresRoles && requiredRoles != null && !ApplicationSecurity.hasAllRoles(requiredRoles))
		{
			final String message = String.format("Insufficient role membership for %s on %s", userName, viewName);
			logger.warn(message);
			throw new AuthorizationException(message);
		}

		try
		{
			RequiresRoles annotation = method.getAnnotation(RequiresRoles.class);
			requiresRoles = (annotation != null);
			requiredRoles = Arrays.asList(annotation.value());
		}
		catch (NullPointerException e)
		{
			requiresRoles = false;
		}

		if (requiresRoles && requiredRoles != null && !ApplicationSecurity.hasAllRoles(requiredRoles))
		{
			final String message = String.format("Insufficient role membership for %s on %s", userName, methodName);
			logger.warn(message);
			throw new AuthorizationException(message);
		}
	}

	/**
	 * Check @RequiresPermissions at the class level.
	 * 
	 * This annotation requires the current executor's Subject to imply a particular permission in order to execute the
	 * annotated method. If the executor's associated Subject determines that the executor does not imply the specified
	 * permission, the method will not be executed.
	 */
	private void CheckRequiresPermissions(Class<?> runtimeClass, Method method, String userName) throws Throwable
	{
		final String viewName = runtimeClass.getSimpleName();
		final String methodName = String.format("%s.%s", viewName, method.getName());

		boolean requiresPermissions = false;
		List<String> requiredPermissions = null;

		try
		{
			RequiresPermissions annotation = runtimeClass.getAnnotation(RequiresPermissions.class);
			requiresPermissions = (annotation != null);
			requiredPermissions = Arrays.asList(annotation.value());
		}
		catch (NullPointerException e)
		{
			requiresPermissions = false;
		}

		if (requiresPermissions && requiredPermissions != null
				&& !ApplicationSecurity.hasAllPermissions(requiredPermissions))
		{
			final String message = String.format("Insufficient permission for %s on %s", userName, viewName);
			logger.warn(message);
			throw new AuthorizationException(message);
		}

		try
		{
			RequiresPermissions annotation = method.getAnnotation(RequiresPermissions.class);
			requiresPermissions = (annotation != null);
			requiredPermissions = Arrays.asList(annotation.value());
		}
		catch (NullPointerException e)
		{
			requiresPermissions = false;
		}

		if (requiresPermissions && requiredPermissions != null
				&& !ApplicationSecurity.hasAllPermissions(requiredPermissions))
		{
			final String message = String.format("Insufficient permission for %s on %s", userName, methodName);
			logger.warn(message);
			throw new AuthorizationException(message);
		}
	}
	
	public class AuthenticationException extends Exception
	{
		private static final long serialVersionUID = -7385155171164478603L;

		public AuthenticationException()
		{
			super();
		}

		public AuthenticationException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public AuthenticationException(String message)
		{
			super(message);
		}

		public AuthenticationException(Throwable cause)
		{
			super(cause);
		}
	}

	public class AuthorizationException extends Exception
	{
		private static final long serialVersionUID = -7385155171164478603L;

		public AuthorizationException()
		{
			super();
		}

		public AuthorizationException(String message, Throwable cause)
		{
			super(message, cause);
		}

		public AuthorizationException(String message)
		{
			super(message);
		}

		public AuthorizationException(Throwable cause)
		{
			super(cause);
		}
	}
}

