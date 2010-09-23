package com.priocept.jcr.server;

import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import junit.framework.TestCase;

import com.google.gwt.dev.util.collect.HashMap;
import com.priocept.jcr.client.domain.JcrNode;

public class TestJcrServiceImpl extends TestCase {
	
	public static final String RMI_URL = "http://localhost:8082/rmi";
	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "admin";
	public static final String WORK_SPACE = "default";
	
	public static final String TEST_NODE1 = "testNode1";
	public static final String TEST_NODE2 = "testNode2";
	
	private JcrServiceImplMock jcrServiceImpl = null;
	
	public TestJcrServiceImpl() {
		try {
			jcrServiceImpl = new JcrServiceImplMock(RMI_URL, WORK_SPACE, USER_NAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGetNewSession() throws Exception {
		Session session = jcrServiceImpl.getNewSession(RMI_URL, WORK_SPACE, USER_NAME, PASSWORD);
		assertTrue(session != null && session.isLive());
	}
	
	public void testAddNewNode() throws Exception {
		String msg = jcrServiceImpl.addNewNode("/", TEST_NODE1, "nt:unstructured", null, false);
		assertTrue(null != msg && msg.equals("New node successfully created."));
		msg = jcrServiceImpl.addNewNode("/", TEST_NODE2, "nt:unstructured", null, false);
		assertTrue(null != msg && msg.equals("New node successfully created."));
	}
	
	public void testGetNodeTree() throws Exception {
		List<Map<String, List<JcrNode>>> nodes = jcrServiceImpl.getNodeTree("/" + TEST_NODE1);
		assertTrue(null != nodes && nodes.size() > 0);
	}
	
	public void testGetNode() throws Exception {
		List<JcrNode> node = jcrServiceImpl.getNode("/" + TEST_NODE1);
		assertTrue(null != node && node.size() == 0);
	}
	
	public void testGetAvailableNodeTypes() throws Exception {
		List<String> nodeTypes = jcrServiceImpl.getAvailableNodeTypes();
		assertTrue(null != nodeTypes && nodeTypes.size() == 52);
	}

	public void testMoveNode() throws Exception {
		String msg = jcrServiceImpl.moveNode("/" + TEST_NODE1, "/" + TEST_NODE2);
		assertTrue(null != msg && msg.startsWith("Successfully moved."));
		msg = jcrServiceImpl.moveNode("/" + TEST_NODE2 + "/" + TEST_NODE1, "/");
		assertTrue(null != msg && msg.startsWith("Successfully moved."));
	}
	
	public void testRenameNode() throws Exception {
		String msg = jcrServiceImpl.renameNode("/" + TEST_NODE1, "123");
		assertTrue(null != msg && msg.startsWith("Successfully renamed from"));
		msg = jcrServiceImpl.renameNode("/123", TEST_NODE1);
		assertTrue(null != msg && msg.startsWith("Successfully renamed from"));
	}

	//Method not implemented
	public void testMoveNodes() throws Exception {
//		String msg = jcrServiceImpl.moveNodes(nodeMap);
//		assertTrue(null != msg && msg.startsWith("Successfully renamed from"));
	}
	
	public void testCutAndPasteNode() throws Exception {
		String msg = jcrServiceImpl.cutAndPasteNode("/" + TEST_NODE1, "/" + TEST_NODE2);
		assertTrue(null != msg && msg.startsWith("Successfully cut and pasted"));
		msg = jcrServiceImpl.cutAndPasteNode("/" + TEST_NODE2 + "/" + TEST_NODE1, "/");
		assertTrue(null != msg && msg.startsWith("Successfully cut and pasted"));
	}
	
	public void testCopyNode() throws Exception {
		String msg = jcrServiceImpl.copyNode("/" + TEST_NODE1, "/" + TEST_NODE2);
		assertTrue(null != msg && msg.startsWith("Successfully copied"));
	}
	
	//Method not implemented
	public void testCopyNodes() throws Exception {
//		String msg = jcrServiceImpl.copyNodes(sourcePath, destinationPath);
//		assertTrue(null != msg && msg.startsWith("Successfully copied"));
	}
	
	//Method not implemented
	public void testSaveNodeDetails() throws Exception {
//		JcrNode jcrNode = new JcrNode();
//		jcrNode.setName("newName");
//		jcrNode.setPath("/newName");
//		jcrNode.setPrimaryNodeType("nt:folder");
//		String msg = jcrServiceImpl.saveNodeDetails("/" + TEST_NODE1, jcrNode);
//		assertTrue(null != msg && msg.startsWith("Successfully saved."));
	}
	
	public void testAddNewProperty() throws Exception {
		String msg = jcrServiceImpl.addNewProperty("/" + TEST_NODE1, "newProperty", "test123");
		assertTrue(null != msg && msg.startsWith("Successfully added new property at"));
	}
	
	public void testDeleteProperty() throws Exception {
		String msg = jcrServiceImpl.deleteProperty("/" + TEST_NODE1, "newProperty");
		assertTrue(null != msg && msg.startsWith("Successfully deleted"));
	}
	
	public void testSaveProperties() throws Exception {
		JcrNode jcrNode = new JcrNode();
		jcrNode.setName("newName");
		jcrNode.setPath("/newName");
		jcrNode.setPrimaryNodeType("nt:folder");
		Map testMap = new HashMap<String, String>();
		testMap.put("testProperty1", "test123");
		testMap.put("testProperty2", "test1234");
		jcrNode.setProperties(testMap);
		String msg = jcrServiceImpl.saveProperties("/" + TEST_NODE1, jcrNode);
		assertTrue(null != msg && msg.startsWith("Successfully saved."));
	}
	
	public void testSavePropertyStringValue() throws Exception {
		String msg = jcrServiceImpl.savePropertyStringValue("/" + TEST_NODE1, "singleSaveProperty", "test321");
		assertTrue(null != msg && msg.startsWith("Successfully saved property"));
	}
	
	//
	public void testSavePropertyBinaryValue() throws Exception {
//		String msg = jcrServiceImpl.addNewNode("/", "testFileNode", "nt:file", null, false);
//		assertTrue(null != msg && msg.equals("New node successfully created."));
//		msg = jcrServiceImpl.savePropertyBinaryValue("/" + TEST_NODE1, property, value);
//		assertTrue(null != msg && msg.startsWith("Successfully saved."));
	}
	
	public void testDeleteNode() throws Exception {
		String msg = jcrServiceImpl.deleteNode("/" + TEST_NODE1);
		assertTrue(null != msg && msg.startsWith("Successfully deleted."));
		msg = jcrServiceImpl.deleteNode("/" + TEST_NODE2);
		assertTrue(null != msg && msg.startsWith("Successfully deleted."));
	}
	
	
}
