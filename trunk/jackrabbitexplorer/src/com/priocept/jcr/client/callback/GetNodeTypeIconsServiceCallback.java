package com.priocept.jcr.client.callback;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class GetNodeTypeIconsServiceCallback implements AsyncCallback<List<Map<String, String>>> {
	public void onSuccess(List<Map<String, String>> result) {
		JackrabbitExplorer.customNodeList = result;
		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn("There was an error: " + caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

