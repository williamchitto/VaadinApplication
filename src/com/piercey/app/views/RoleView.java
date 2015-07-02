package com.piercey.app.views;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.piercey.app.entities.Role;
import com.piercey.app.framework.DatabaseUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class RoleView extends Panel implements View
{
	private static final long serialVersionUID = -7650094489274659106L;
	
	public static final String NAME = "RoleView";

	public RoleView()
	{
		setCaption("role");
		
		final TextField username = new TextField("Username");
		addComponent(username);

		final PasswordField password = new PasswordField("Password");
		addComponent(password);
		
		SessionFactory sessionFactory = DatabaseUtil.getSessionFactory();
		Session openSession = sessionFactory.openSession();
		openSession.beginTransaction();
			
		Role role = new Role();
		role.setDescription("william");
		role.setRoleName("role william2");
		openSession.save(role);
		
		openSession.getTransaction().commit();
		openSession.flush();
		
		
		
	}

	
	@Override
	public void enter(ViewChangeEvent event)
	{
		if (event.getParameters() != null)
		{
			String[] msgs = event.getParameters().split("/");
			for (String msg : msgs)
			{
				addComponent(new Label(msg));
			}
		}
	}
}
