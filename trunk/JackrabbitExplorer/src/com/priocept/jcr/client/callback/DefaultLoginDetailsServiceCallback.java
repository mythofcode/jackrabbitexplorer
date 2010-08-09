package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.priocept.jcr.client.domain.LoginDetails;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class DefaultLoginDetailsServiceCallback implements AsyncCallback<LoginDetails> {
	private JackrabbitExplorer jackrabbitExplorer;
	public DefaultLoginDetailsServiceCallback(JackrabbitExplorer jackrabbitExplorer) {
		this.jackrabbitExplorer = jackrabbitExplorer;
	}
	public void onSuccess(LoginDetails result) {
		jackrabbitExplorer.loginDetails = result;
		JackrabbitExplorer.hideLoadingImg();
		jackrabbitExplorer.showLoginBox();
	}

	public void onFailure(Throwable caught) {
		SC.warn("There was an error: " + caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

