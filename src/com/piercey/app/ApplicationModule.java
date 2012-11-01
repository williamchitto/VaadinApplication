package com.piercey.app;

import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.vaadin.ui.UI;

public class ApplicationModule extends ServletModule
{
	@Override
	protected void configureServlets()
	{
		serve("/*").with(ApplicationServlet.class);
		bind(String.class).annotatedWith(Names.named("title")).toInstance("Vaadin Application");
		bind(String.class).annotatedWith(Names.named("version")).toInstance("<b>Vaadin 7 Beta 7</b>");
	}

	@Provides
	private Class<? extends UI> provideUIClass()
	{
		return ApplicationUI.class;
	}
}
