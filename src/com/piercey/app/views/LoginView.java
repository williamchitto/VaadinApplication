package com.piercey.app.views;

import com.piercey.app.security.SecurityCenter;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class LoginView extends Panel implements View
{
	private static final long serialVersionUID = 6151712847910060303L;
	public static final String NAME = "LoginView";

	public LoginView()
	{
		this(null);
	}

	public LoginView(final String fragmentAndParameters)
	{
		setCaption("Login");

		final TextField username = new TextField("Username");
		addComponent(username);

		final PasswordField password = new PasswordField("Password");
		addComponent(password);

		final CheckBox rememberMe = new CheckBox("Remember Me");
		addComponent(rememberMe);

		username.focus();
		
		if (SecurityCenter.isRemembered())
		{
			username.setValue(SecurityCenter.whoIsRemembered());
			rememberMe.setValue(SecurityCenter.isRemembered());
			password.focus();
		}

		@SuppressWarnings("serial")
		final Button login = new Button("Login", new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				final Navigator navigator = UI.getCurrent().getNavigator();
				if (SecurityCenter.login(username.getValue(), password.getValue(), rememberMe.getValue()))
				{
					final String location = (fragmentAndParameters == null)
							? ApplicationView.NAME
							: fragmentAndParameters;
					
					navigator.navigateTo(location);
				}
				else
				{
					navigator.navigateTo(LoginView.NAME);
				}
			}
		});
		addComponent(login);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
	}
}

