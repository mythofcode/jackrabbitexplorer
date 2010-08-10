package com.priocept.jcr.client.callback;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class AvailableNodeTypesServiceCallback implements AsyncCallback<List<String>> {
	public static String DEFAULT_NODE_TYPE = "nt:unstructured";
	public static LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();

	public void onSuccess(List<String> result) {
		List<String> returnList = result;
		if (null == returnList || returnList.size() < 1) {
			SC.say("No available Node Types found.");
			JackrabbitExplorer.hideLoadingImg();
			return;
		}
		
		for (Iterator<String> iterator = returnList.iterator(); iterator.hasNext();) {
			String nodeType = (String) iterator.next();
			valueMap.put(nodeType, nodeType);
		}
		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn("There was an error: " + caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

