package com.piercey.app;

import com.google.inject.Inject;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class ApplicationProvider extends UIProvider
{
	private static final long serialVersionUID = -7515940655980636234L;
	
	@Inject
	private Class<? extends UI> applicationClass;

	@Override
	public UI createInstance(UICreateEvent event)
	{
		return ApplicationFilter.getInjector().getProvider(applicationClass).get();
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event)
	{
		return applicationClass;
	}
}
