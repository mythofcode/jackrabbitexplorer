package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;

/**
 * @author chrisjennings
 *
 */
public class ChangeNodeTypeIconAssociationCallback implements AsyncCallback<Boolean> {
	private JackrabbitExplorer jackrabbitExplorer;

	public ChangeNodeTypeIconAssociationCallback(JackrabbitExplorer jackrabbitExplorer) {
		this.jackrabbitExplorer = jackrabbitExplorer;
	}

	@Override
	public void onFailure(Throwable throwable) {
		//SC.warn("There was an error: " + throwable.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}

	@Override
	public void onSuccess(Boolean arg0) {
		jackrabbitExplorer.getNodeTypeIcons();
		jackrabbitExplorer.refreshFromRoot();
	}
}
