package com.priocept.jcr.server;

import javax.jcr.Session;


public class JcrServiceImplMock extends JcrServiceImpl {
	
	private Session session = null;
	
	public JcrServiceImplMock(String RMI_URL, String WORK_SPACE, String USER_NAME, String PASSWORD) throws Exception {
		session = getNewSession(RMI_URL, WORK_SPACE, USER_NAME, PASSWORD);
	}
	
	protected Session getJcrSession() throws Exception {
		return this.session;
	}
	
}
