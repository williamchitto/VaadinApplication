package com.piercey.app;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.piercey.app.framework.ApplicationFilter;
import com.piercey.app.framework.ApplicationLogger;
import com.piercey.app.framework.ApplicationSecurity;
import com.piercey.app.framework.DatabaseUtil;
import com.piercey.app.views.ApplicationView;
import com.piercey.app.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ApplicationUI extends UI
{
	private static final long serialVersionUID = 6786857451870760567L;
	private static final ApplicationLogger logger = new ApplicationLogger(ApplicationUI.class);
	public static final String LOGINVIEW = LoginView.NAME;
	
	@Inject
	@Named("title")
	private String title = "Vaadin Application UI (default title)";

	@Inject(optional = true)
	@Named("version")
	private String version = "Vaadin <i>version unknown</i>";

	private VerticalLayout layout = new VerticalLayout();  
	
	@Override
	public void init(VaadinRequest request)
	{
		logger.executionTrace();
		DatabaseUtil.getSessionFactory(); // Initialize...

		final Navigator navigator = new Navigator(this, layout);
		setNavigator(navigator);
		this.setContent(layout);		
		navigator.addProvider(new ViewProvider()
		{
			private static final long serialVersionUID = -3308179049710571790L;

			@Override
			public String getViewName(String viewAndParameters)
			{
				if (viewAndParameters == null || viewAndParameters.length() == 0)
					return ApplicationView.class.getSimpleName();
				
				String[] parts = viewAndParameters.split("/");
				return parts[0];
			}
			
			@Override
			public View getView(String viewName)
			{
				final Injector injector = ApplicationFilter.getSecurityInjector();
				final String packageName = ApplicationView.class.getPackage().getName();
				Class<?> classType = ApplicationView.class;

				try
				{
					classType = Class.forName(packageName + "." + viewName);
				}
				catch (ClassNotFoundException e)
				{
				}
				
				return (View) injector.getInstance(classType);
			}
		});
		
		final String viewName = (ApplicationSecurity.isAuthenticated())
				? ApplicationView.NAME
				: LoginView.NAME;

		navigator.navigateTo(viewName);
	}
}
