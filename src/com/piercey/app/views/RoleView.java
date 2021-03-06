package com.piercey.app.views;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.piercey.app.entities.Account;
import com.piercey.app.entities.Permission;
import com.piercey.app.entities.Role;
import com.piercey.app.framework.DatabaseUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class RoleView extends VerticalLayout implements View {
	private static final long serialVersionUID = -7650094489274659106L;

	public static final String NAME = "RoleView";

	public List<Role> list;

	;

	public RoleView() {

		SessionFactory sessionFactory = DatabaseUtil.getSessionFactory();
		Session openSession = sessionFactory.openSession();
		openSession.beginTransaction();

		Query query = openSession.createQuery("from Role");

		list = query.list();

		for (Role acconunt : list) {
			addComponent(new Label(acconunt.getRoleName()));

		}

		openSession.getTransaction().commit();
		openSession.flush();

		Button button = new Button("roles");

		button.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				for (Role account : list) {
					for (Permission role : account.getPermissions()) {
						System.out.println(role.getPermissionName());
					}
				}
			}
		});

		addComponent(button);

	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (event.getParameters() != null) {
			String[] msgs = event.getParameters().split("/");
			for (String msg : msgs) {
				addComponent(new Label(msg));
			}
		}
	}
}
