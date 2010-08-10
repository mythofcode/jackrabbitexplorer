package com.priocept.jcr.client.callback;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.priocept.jcr.client.domain.JcrNode;
import com.priocept.jcr.client.domain.JcrTreeNode;
import com.smartgwt.client.util.SC;

/**
 * 
 * @author James Pickup
 *
 */
public class GetNodeServiceCallback implements AsyncCallback<List<JcrNode>> {
	private JackrabbitExplorer jackrabbitExplorer;
	private String parentPath;
	public GetNodeServiceCallback(JackrabbitExplorer jackrabbitExplorer, String parentPath) {
		this.jackrabbitExplorer = jackrabbitExplorer;
		this.parentPath = parentPath;
	}
	public void onSuccess(List<JcrNode> result) {
		List<JcrNode> jcrNodeList = result;
		JcrTreeNode[] jcrTreeNodes = new JcrTreeNode[jcrNodeList
				.size()];
		int x = 0;
		for (JcrNode jcrNode : jcrNodeList) {
			jcrTreeNodes[x] = new JcrTreeNode(jcrNode.getName(), jcrNode.getPath(), jcrNode.getPrimaryNodeType(), jcrNode.getProperties());
			JackrabbitExplorer.setCustomTreeIcon(jcrTreeNodes[x], jcrNode.getPrimaryNodeType());
			x++;
		}
		JcrTreeNode parentAnimateTreeNode;
		if (parentPath != null) {
			parentAnimateTreeNode = (JcrTreeNode) jackrabbitExplorer.jcrTree.find("/root" + parentPath);
			jackrabbitExplorer.jcrTreeGrid.setData(jackrabbitExplorer.jcrTree);
		} else { 
			parentAnimateTreeNode = (JcrTreeNode) jackrabbitExplorer.jcrTreeGrid.getSelectedRecord();
		}
		jackrabbitExplorer.jcrTree.addList(jcrTreeNodes, parentAnimateTreeNode);
		jackrabbitExplorer.jcrTreeGrid.setData(jackrabbitExplorer.jcrTree);

		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn(caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

