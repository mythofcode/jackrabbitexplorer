package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class GetBrowsableContentFilterRegexsServiceCallback implements AsyncCallback<String> {
	public void onSuccess(String result) {
		JackrabbitExplorer.browsableContentFilterRegex = result;
		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn("There was an error: " + caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

