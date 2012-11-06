package com.piercey.app.framework;

import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@Singleton
public class ApplicationServlet extends VaadinServlet implements SessionInitListener
{
	private static final long serialVersionUID = -2187756856751047938L;
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationServlet.class);

	@Inject
	private ApplicationProvider applicationProvider;
	
	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException
	{
		logger.executionTrace();
		event.getSession().addUIProvider(applicationProvider);
	}
	
	@Override
	protected DeploymentConfiguration createDeploymentConfiguration(Properties initParameters)
	{
		logger.executionTrace();
		initParameters.setProperty(SERVLET_PARAMETER_PRODUCTION_MODE, "true");
		return super.createDeploymentConfiguration(initParameters);
	}

	@Override
	protected void servletInitialized()
	{
		logger.executionTrace();
		getService().addSessionInitListener(this);
	}
}
