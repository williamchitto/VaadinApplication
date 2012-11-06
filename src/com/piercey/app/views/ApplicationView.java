package com.piercey.app.views;

import com.piercey.app.ApplicationSecurity;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class ApplicationView extends Panel implements View
{
	private static final long serialVersionUID = -1307009854319440228L;
	public static final String NAME = "ApplicationView";

	public ApplicationView()
	{
		Link lnk = new Link("Count", new ExternalResource("#!" + CountView.NAME));
		addComponent(lnk);

		lnk = new Link("Message: Hello", new ExternalResource("#!" + MessageView.NAME + "/Hello"));
		addComponent(lnk);

		lnk = new Link("Message: Bye", new ExternalResource("#!" + MessageView.NAME + "/Bye/Goodbye"));
		addComponent(lnk);

		@SuppressWarnings("serial")
		Button logout = new Button("Logout", new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				ApplicationSecurity.logout();
				UI.getCurrent().getNavigator().navigateTo(LoginView.NAME);
			}
		});
		addComponent(logout);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
	}
}
