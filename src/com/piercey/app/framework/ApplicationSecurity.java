package com.piercey.app.framework;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ApplicationSecurity
{
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationSecurity.class);

	public static boolean login(String username, String password, boolean rememberMe)
	{
		logger.executionTrace();
		final Subject subject = SecurityUtils.getSubject();

		try
		{
			logger.info(String.format("Login requested for %s", subject.getPrincipal()));

			if (!subject.isAuthenticated())
			{
				final UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				token.setRememberMe(rememberMe);
				subject.login(token);
			}

			logger.info(String.format("Login succeeded for %s", subject.getPrincipal()));
			return true;
		}
		catch (UnknownAccountException e)
		{
			logger.warn(String.format("Login failed for %s: %s", username, e.getMessage()));
			logger.debug(e);
			Notification.show("Invalid Credentials", Type.WARNING_MESSAGE);
		}
		catch (IncorrectCredentialsException e)
		{
			logger.warn(String.format("Login failed for %s: %s", username, e.getMessage()));
			logger.debug(e);
			Notification.show("Invalid Credentials", Type.WARNING_MESSAGE);
		}
		catch (LockedAccountException e)
		{
			logger.warn(String.format("Login failed for %s: %s", username, e.getMessage()));
			logger.debug(e);
			Notification.show("Account is Locked", Type.WARNING_MESSAGE);
		}
		catch (AuthenticationException e)
		{
			logger.error(e);
			Notification.show("Unknown Security Error", Type.WARNING_MESSAGE);
		}
		catch (Exception e)
		{
			logger.error(e);
			Notification.show("Unknown System Error", Type.WARNING_MESSAGE);
		}

		logger.info(String.format("Login failed for %s", username));
		return false;
	}

	public static void logout()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();

		if (subject.isAuthenticated())
		{
			subject.logout();
			logger.info(String.format("Logout succeeded for %s", subject.getPrincipal()));
		}
	}

	public static boolean isAuthenticated()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.isAuthenticated();
	}

	public static String getPrincipal()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return (String) subject.getPrincipal();
	}

	public static boolean isRemembered()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.isRemembered();
	}

	public static boolean isGuest()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return (subject.getPrincipal() == null);
	}

	public static String whoIsRemembered()
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return (String) subject.getPrincipal();
	}

	public static boolean hasRole(String role)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.hasRole(role);
	}

	public static boolean[] hasRoles(List<String> roles)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.hasRoles(roles);
	}

	public static boolean hasAllRoles(List<String> roles)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.hasAllRoles(roles);
	}

	public static boolean hasPermission(String permission)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(permission);
	}

	public static boolean[] hasPermission(List<String> permissions)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();
		final boolean[] permissionList = new boolean[permissions.size()];

		for (int i = 0; i < permissions.size(); i++)
			permissionList[i] = subject.isPermitted(permissions.get(i));

		return permissionList;
	}

	public static boolean hasAllPermissions(List<String> permissions)
	{
		logger.executionTrace();

		final Subject subject = SecurityUtils.getSubject();

		for (String permission : permissions)
			if (!subject.isPermitted(permission))
				return false;

		return true;
	}
}
