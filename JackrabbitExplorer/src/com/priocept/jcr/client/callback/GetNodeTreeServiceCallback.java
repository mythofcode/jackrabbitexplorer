package com.priocept.jcr.client.callback;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.priocept.jcr.client.JackrabbitExplorer;
import com.priocept.jcr.client.domain.JcrNode;
import com.priocept.jcr.client.domain.JcrTreeNode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 
 * @author James Pickup
 *
 */
public class GetNodeTreeServiceCallback implements AsyncCallback<List<Map<String, List<JcrNode>>>> {
	private JackrabbitExplorer jackrabbitExplorer;
	public GetNodeTreeServiceCallback(JackrabbitExplorer jackrabbitExplorer) {
		this.jackrabbitExplorer = jackrabbitExplorer;
	}
	public void onSuccess(List<Map<String, List<JcrNode>>> result) {
		List<Map<String, List<JcrNode>>> jcrNodesList = result;
		JcrTreeNode[] jcrTreeNodes = null;
		for (Map<String, List<JcrNode>> treeAssociationMap : jcrNodesList) {
			for (Iterator<Map.Entry<String, List<JcrNode>>> iterator = treeAssociationMap.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry<String, List<JcrNode>> pairs = (Map.Entry<String, List<JcrNode>>)iterator.next();
				jcrTreeNodes = new JcrTreeNode[pairs.getValue().size()];
				int x = 0;
				for(JcrNode jcrNode : pairs.getValue()) {
					jcrTreeNodes[x] = new JcrTreeNode(jcrNode.getName(), jcrNode.getPath(), jcrNode.getPrimaryNodeType(), jcrNode.getProperties());
					JackrabbitExplorer.setCustomTreeIcon(jcrTreeNodes[x], jcrNode.getPrimaryNodeType());
					x++;
				}
				
				TreeNode tempTreeNode = jackrabbitExplorer.jcrTree.find("/root" + pairs.getKey().toString());
				if (null != tempTreeNode) {
					if (!jackrabbitExplorer.jcrTree.hasChildren(tempTreeNode)) {
						jackrabbitExplorer.jcrTree.addList(jcrTreeNodes, "/root" + pairs.getKey().toString());
					}
				}
				jackrabbitExplorer.jcrTree.openFolder(tempTreeNode);
			}
		}
		jackrabbitExplorer.jcrTreeGrid.setData(jackrabbitExplorer.jcrTree);
		JackrabbitExplorer.hideLoadingImg();
	}

	public void onFailure(Throwable caught) {
		SC.warn(caught.toString(), new NewBooleanCallback());
		JackrabbitExplorer.hideLoadingImg();
	}
}

