package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class AddNodeTypesServiceCallback implements AsyncCallback<Boolean>
{
	public void onSuccess(Boolean result) {
		SC.say("Added CND node types successfully.");
		JackrabbitExplorer.hideLoadingImg();		
	}
	
	public void onFailure(Throwable caught) {
		SC.warn(caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}
