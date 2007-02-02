package com.google.enterprise.connector.dctm.dctmdfcwrap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfAttr;
import com.google.enterprise.connector.dctm.DmInitialize;
import com.google.enterprise.connector.dctm.dfcwrap.IClient;
import com.google.enterprise.connector.dctm.dfcwrap.IClientX;
import com.google.enterprise.connector.dctm.dfcwrap.IFormat;

import com.google.enterprise.connector.dctm.dfcwrap.ILoginInfo;
import com.google.enterprise.connector.dctm.dfcwrap.ISession;
import com.google.enterprise.connector.dctm.dfcwrap.ISessionManager;

import com.google.enterprise.connector.dctm.dfcwrap.ISysObject;
import com.google.enterprise.connector.spi.RepositoryException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DmSysObjectATest extends TestCase {

	ISysObject object;

	ISession session = null;

	ISessionManager sessionManager;

	public void setUp() throws Exception {
		super.setUp();
		IClientX dctmClientX;
		IClient localClient;

		ILoginInfo loginInfo;
		dctmClientX = new DmClientX();
		localClient = dctmClientX.getLocalClient();
		sessionManager = localClient.newSessionManager();
		loginInfo = dctmClientX.getLoginInfo();
		loginInfo.setUser(DmInitialize.DM_LOGIN_OK1);
		loginInfo.setPassword(DmInitialize.DM_PWD_OK1);
		sessionManager.setIdentity(DmInitialize.DM_DOCBASE, loginInfo);

		session = sessionManager.getSession(DmInitialize.DM_DOCBASE);
		object = session.getObject(dctmClientX.getId(DmInitialize.DM_ID1));

	}

	public void testGetFormat() throws RepositoryException {
		try {
			IFormat format = object.getFormat();
			Assert.assertNotNull(format);
		} finally {
			if (session != null) {
				sessionManager.release(session);
			}
		}
	}

	public void testGetContentSize() throws RepositoryException {
		try {
			long size = object.getContentSize();
			assertTrue(size > 0);
		} finally {
			if (session != null) {
				sessionManager.release(session);
			}
		}

	}

	public void testGetContent() throws DfException, RepositoryException,
	IOException{
		try{
			DmDocument document= CreateNewDocument(session);
			ByteArrayInputStream content = ((DmSysObject)document).getContent();
			Assert.assertNotNull(content);
			deleteDocument(document);
		}finally{
			if(session != null){
				sessionManager.release(session);
			}
		}
	}
			
	
	
		
	public void testEnumAttrs() throws DfException, RepositoryException,
	IOException{
		DmDocument document= CreateNewDocument(session);
		Enumeration attrs = ((DmSysObject)document).enumAttrs();
		Assert.assertNotNull(attrs);
		while (attrs.hasMoreElements()){
			IDfAttr curAttr = (IDfAttr) attrs.nextElement();
			String name = curAttr.getName();
			System.out.println("name vaut "+name);
			if (name.equals("object_name")){
				String object_name=document.getString("object_name");
				Assert.assertEquals(object_name,"Document creation test");
				// break;
			}
			// break;
		}
		deleteDocument(document);
	}
		
		
		
		
		
		public void testGetACLDomain() throws DfException, RepositoryException,
		IOException{
			DmDocument document= CreateNewDocument(session);
			String ACLDomain = ((DmSysObject)document).getACLDomain();
			System.out.println("acldomain vaut "+ACLDomain);
			Assert.assertNotNull(ACLDomain);
			Assert.assertEquals(ACLDomain,DmInitialize.DM_LOGIN_OK1);
			deleteDocument(document);
		}
		
		
		public void testGetACLName() throws DfException, RepositoryException,
		IOException{
			DmDocument document= CreateNewDocument(session);
			String ACLName = ((DmSysObject)document).getACLName();
			System.out.println("aclname vaut "+ACLName);
			Assert.assertNotNull(ACLName);
			deleteDocument(document);
		}
		
		public DmDocument CreateNewDocument(ISession session) throws
		RepositoryException, IOException{
			DmDocument document=((DmSession)session).newObject();
			File f=new File("DocumentCreationTest.txt");
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject("Foundation Course Content Outline Overview Concepts The mission of Google and Google Enterprise");
					document.setFileEx("DocumentCreationTest.txt","text");
					document.setObjectName("Document creation test");
					document.save();
					oos.close();
					boolean del=f.delete();
					System.out.println("del vaut "+del);
					return document;
		}
		
		public static void deleteDocument(DmDocument document) throws
		RepositoryException{
			document.destroyAllVersions();
		}

}
