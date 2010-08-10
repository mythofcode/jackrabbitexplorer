package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class LoginServiceCallback implements AsyncCallback<Boolean> {
	private JackrabbitExplorer jackrabbitExplorer;
	public LoginServiceCallback(JackrabbitExplorer jackrabbitExplorer) {
		this.jackrabbitExplorer = jackrabbitExplorer;
	}
	public void onSuccess(Boolean result) {
		JackrabbitExplorer.hideLoadingImg();
		JackrabbitExplorer.service.getAvailableNodeTypes(new AvailableNodeTypesServiceCallback());
		jackrabbitExplorer.drawMainLayout();
	}

	public void onFailure(Throwable caught) {
		SC.warn("There was an error logging in: " + caught.toString(), new LoginErrorCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
	
	public class LoginErrorCallback implements BooleanCallback {
		public void execute(Boolean value) {
			JackrabbitExplorer.loginWindow.show();
			return;
		}
	}
}

