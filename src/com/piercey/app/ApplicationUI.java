package com.piercey.app;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ApplicationUI extends UI
{
	private static final long serialVersionUID = 6786857451870760567L;

	@Inject
	@Named("title")
	private String title = "Vaadin Application UI (default title)";

	@Inject(optional = true)
	@Named("version")
	private String version = "Vaadin <i>version unknown</i>";

	@Override
	public void init(VaadinRequest request)
	{
		getPage().setTitle(title);

		Label label = new Label(version, ContentMode.HTML);
		label.setSizeUndefined();

		VerticalLayout layout = new VerticalLayout(label);
		layout.setSizeFull();
		layout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);

		setContent(layout);
	}
}
