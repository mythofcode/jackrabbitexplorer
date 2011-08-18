package com.priocept.jcr.client.handler;

import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;

/**
 * @author chrisjennings
 * 
 */
public class IconGridHandler implements CellClickHandler {

	private JackrabbitExplorer jackrabbitExplorer;

	public IconGridHandler(JackrabbitExplorer jackrabbitExplorer) {
		this.jackrabbitExplorer = jackrabbitExplorer;
	}

	@Override
	public void onCellClick(CellClickEvent event) {
		jackrabbitExplorer.changeCurrentNodeTypeAssociation(event.getRecord()
				.getAttribute("path"));
		jackrabbitExplorer.hidePossibleIconsWindow();
	}
}
