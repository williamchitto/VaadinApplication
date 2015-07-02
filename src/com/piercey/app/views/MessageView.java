package com.piercey.app.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class MessageView extends Panel implements View
{
	private static final long serialVersionUID = -7650094489274659106L;
	
	public static final String NAME = "MessageView";

	public MessageView()
	{
		setCaption("Messages");
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
