package com.priocept.jcr.client.callback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class CRUDServiceCallback implements AsyncCallback<String> {
	private JackrabbitExplorer jackrabbitExplorer;
	String newNodePath;
	String deletedNodePath;
	public CRUDServiceCallback(JackrabbitExplorer jackrabbitExplorer, String newNodePath, String deletedNodePath) {
		this.jackrabbitExplorer = jackrabbitExplorer;
		this.newNodePath = newNodePath;
		this.deletedNodePath = deletedNodePath;
	}
	public void onSuccess(String result) {
		String returnMessage = result;
		//if returMessage says the operation failed. 
			//return
		
		if (null != newNodePath && !newNodePath.equals("")) {
			jackrabbitExplorer.treeRecordClick(jackrabbitExplorer.jcrTreeGrid, true, newNodePath);
		}
		if (null != deletedNodePath && !deletedNodePath.equals("")) {
			jackrabbitExplorer.treeDeleteUpdate(getParentPath(deletedNodePath));
		}
		
		SC.say(returnMessage);
		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn(caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
	
	private static String getParentPath(String path) {
		String parentPath = path.substring(0, path.lastIndexOf('/'));
		if (null != parentPath && parentPath.equals("")) {
			return "/";
		}
		return parentPath;
	}

}

