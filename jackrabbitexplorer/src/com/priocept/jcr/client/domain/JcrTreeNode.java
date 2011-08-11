package com.priocept.jcr.client.domain;

import java.io.Serializable;
import java.util.Map;

import com.priocept.jcr.client.JackrabbitExplorer;
import com.smartgwt.client.widgets.tree.TreeNode;

public class JcrTreeNode extends TreeNode implements Serializable {
	private static final long serialVersionUID = 2162736740069966873L;
	private Map<String, String> properties;
	
	public JcrTreeNode() {
		super();
	}

	public JcrTreeNode(String name, String path, String primaryNodeType,
			Map<String, String> properties, JcrTreeNode... children) {
		setTitle(name);
		setAttribute("path", path);
		setAttribute("primaryNodeType", primaryNodeType);
		this.properties = properties;
		setAttribute("treeGridIcon", JackrabbitExplorer.defaultIcon);
		setChildren(children);
	}

	public JcrTreeNode(String name, String path, String primaryNodeType, 
			Map<String, String> properties, String treeGridIcon, JcrTreeNode... children) {
		setTitle(name);
		setAttribute("path", path);
		setAttribute("primaryNodeType", primaryNodeType);
		this.properties = properties;
		//setAttribute("treeGridIcon", JackrabbitExplorer.defaultIcon);
		setAttribute("treeGridIcon", treeGridIcon);
		setChildren(children);
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
