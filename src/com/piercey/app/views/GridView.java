package com.piercey.app.views;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.piercey.app.entities.Permission;
import com.piercey.app.entities.Role;
import com.piercey.app.framework.DatabaseUtil;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class GridView extends VerticalLayout implements View {
	private static final long serialVersionUID = -7650094489274659106L;

	public static final String NAME = "GridView";

	public List<Role> list;


	  private Grid grid;
	  
	public GridView() {

		SessionFactory sessionFactory = DatabaseUtil.getSessionFactory();
	/*	Session openSession = sessionFactory.openSession();
		openSession.beginTransaction();

	//	Query query = openSession.createQuery("from Role");

		//list = query.list();

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
		
		*/
		
		
		JPAContainer<Role> container = new JPAContainer<Role>(Role.class);
		
		
///	HbnContainer<Role> container = new HbnContainer<Role>(Role.class, sessionFactory);
	 grid = new Grid();
     grid.setSizeFull();
     
     
     grid.setContainerDataSource(container);
     addComponent(grid);
	
		
		

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
