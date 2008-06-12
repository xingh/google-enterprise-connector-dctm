package com.google.enterprise.connector.dctm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.google.enterprise.connector.dctm.dfcwrap.IAttr;
import com.google.enterprise.connector.dctm.dfcwrap.IClientX;
import com.google.enterprise.connector.dctm.dfcwrap.IFormat;
import com.google.enterprise.connector.dctm.dfcwrap.IId;
import com.google.enterprise.connector.dctm.dfcwrap.ISession;
import com.google.enterprise.connector.dctm.dfcwrap.ISessionManager;
import com.google.enterprise.connector.dctm.dfcwrap.ISysObject;
import com.google.enterprise.connector.dctm.dfcwrap.ITime;
import com.google.enterprise.connector.dctm.dfcwrap.IValue;
import com.google.enterprise.connector.spi.Document;
import com.google.enterprise.connector.spi.Property;
import com.google.enterprise.connector.spi.RepositoryException;
import com.google.enterprise.connector.spi.SpiConstants;
import com.google.enterprise.connector.spiimpl.BinaryValue;
import com.google.enterprise.connector.spiimpl.BooleanValue;
import com.google.enterprise.connector.spiimpl.DoubleValue;
import com.google.enterprise.connector.spiimpl.LongValue;
import com.google.enterprise.connector.spiimpl.StringValue;

public class DctmSysobjectDocument extends HashMap implements Document {

	private static final long serialVersionUID = 126421624L;

	private String docId;

	private ISysObject object = null;

	private ISessionManager sessionManager = null;

	private IClientX clientX;

	private String isPublic = "false";

	private String versionId;

	private HashSet included_meta;

	private HashSet excluded_meta;

	private String object_id_name = "r_object_id";

	private static Logger logger = null;

	static {
		logger = Logger.getLogger(DctmSysobjectDocument.class.getName());
	}

	public DctmSysobjectDocument(String docid, ISessionManager sessionManager,
			IClientX clientX, String isPublic, HashSet included_meta,
			HashSet excluded_meta) {
		this.docId = docid;
		this.sessionManager = sessionManager;
		this.clientX = clientX;
		this.isPublic = isPublic;
		this.included_meta = included_meta;
		this.excluded_meta = excluded_meta;
	}

	private void fetch() throws RepositoryException {
		if (object != null) {
			return;
		}
		ISession session = null;
		try {
			String docbaseName = sessionManager.getDocbaseName();
			session = sessionManager.getSession(docbaseName);
			
			logger.info("Get a session for the docbase "+docbaseName);
			
			IId id = clientX.getId(docId);
			
			logger.info("r_object_id of the fetched object is "+id);
			
			object = session.getObject(id);
			
			versionId = object.getId("i_chronicle_id").getId();
			
			logger.fine("i_chronicle_id of the fetched object is "+versionId);
			
			object.setSessionManager(sessionManager);
		} finally {
			if (session != null) {
				sessionManager.release(session);
				logger.fine("session released");
			}
		}
	}

	public Property findProperty(String name) throws RepositoryException {
		IFormat dctmForm = null;
		String mimetype = "";
		String dosExtension= "";
		long contentSize=0;
		fetch();
		HashSet hashSet;
		hashSet = new HashSet();
		if (name.equals(SpiConstants.PROPNAME_DOCID)) {
			hashSet.add(new StringValue(versionId));
			logger.fine("property "+SpiConstants.PROPNAME_DOCID+" has the value "+versionId);
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_CONTENT.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_CONTENT);
			hashSet.add(new BinaryValue(object.getContent()));
			logger.fine("property "+SpiConstants.PROPNAME_CONTENT+" after getContent");
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_DISPLAYURL.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_DISPLAYURL);
			hashSet.add(new StringValue(sessionManager.getServerUrl() + docId));
			logger.fine("property "+SpiConstants.PROPNAME_DISPLAYURL+" has the value "+sessionManager.getServerUrl() + docId);
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_SECURITYTOKEN.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_SECURITYTOKEN);
			hashSet.add(new StringValue(object.getACLDomain() + " "
					+ object.getACLName()));
			logger.fine("property "+SpiConstants.PROPNAME_SECURITYTOKEN+" has the value "+object.getACLDomain() + " "
					+ object.getACLName());
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_ISPUBLIC.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_ISPUBLIC);
			hashSet.add(BooleanValue.makeBooleanValue(this.isPublic
					.equals("true")));
			logger.fine("property "+SpiConstants.PROPNAME_ISPUBLIC+" set to true");
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_LASTMODIFIED.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_LASTMODIFIED);
			hashSet.add(new DctmDateValue(getDate("r_modify_date")));
			logger.fine("property "+SpiConstants.PROPNAME_LASTMODIFIED+" has the value "+getDate("r_modify_date"));
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_MIMETYPE.equals(name)) {
			logger.fine("getting the property "+SpiConstants.PROPNAME_MIMETYPE);
			dctmForm = object.getFormat();
			mimetype = dctmForm.getMIMEType();
			dosExtension = dctmForm.getDOSExtension();
			contentSize= object.getContentSize();
			hashSet.add(new StringValue(mimetype));
			logger.fine("property "+SpiConstants.PROPNAME_MIMETYPE+" has the value "+mimetype);
			logger.fine("mimetype of the document "+versionId+" : "+mimetype);
			logger.fine("dosExtension of the document "+versionId+" : "+dosExtension);
			logger.fine("contentSize of the document "+versionId+" : "+contentSize);
			return new DctmSysobjectProperty(name, hashSet);
		} else if (SpiConstants.PROPNAME_SEARCHURL.equals(name)) {
			return null;
		} 
		else if (SpiConstants.PROPNAME_ACTION.equals(name)){
			return null;
		}
		else if (object_id_name.equals(name)) {
			logger.fine("getting the property "+object_id_name);
			hashSet.add(new StringValue(docId));
			logger.fine("property "+object_id_name+" has the value "+docId);
			return new DctmSysobjectProperty(name, hashSet);
		}
		IAttr attr = object.getAttr(object.findAttrIndex(name));
		logger.finer("the attribute "+ name + " is in the position "+ object.findAttrIndex(name)+ " in the list of attributes of the fetched object");
		
		int i = object.getValueCount(name);
		logger.finer("the attribute "+ name + " stores "+ i + " values ");
		
		IValue val = null;
		for (int j = 0; j < i; j++) {
			val = object.getRepeatingValue(name, j);
			logger.finer("getting the value of index "+ j +" of the attribute "+ name);
			try {				
				if (attr.getDataType() == IAttr.DM_BOOLEAN) {
					logger.finer("the attribute of index "+ j +" is of boolean type");
					hashSet.add(BooleanValue.makeBooleanValue(val.asBoolean()));
				} else if (attr.getDataType() == IAttr.DM_DOUBLE) {
					logger.finer("the attribute of index "+ j +" is of double type");
					hashSet.add(new DoubleValue(val.asDouble()));
				} else if (attr.getDataType() == IAttr.DM_ID) {
					logger.finer("the attribute of index "+ j +" is of ID type");
					hashSet.add(new StringValue(object.getId(name).getId()));
				} else if (attr.getDataType() == IAttr.DM_INTEGER) {
					logger.finer("the attribute of index "+ j +" is of integer type");
					hashSet.add(new LongValue(val.asInteger()));
				} else if (attr.getDataType() == IAttr.DM_STRING) {
					logger.finer("the attribute of index "+ j +" is of String type");
					hashSet.add(new StringValue(val.asString()));
				} else if (attr.getDataType() == IAttr.DM_TIME) {
					logger.finer("the attribute of index "+ j +" is of date type");
					hashSet.add(new DctmDateValue(getCalendarFromDate(val.asTime().getDate())));
				}
				
			} catch (Exception e) {
				logger.warning("exception is thrown when getting the value of index "+ j +" of the attribute "+ name);
				logger.warning("exception "+e.getMessage());
				hashSet.add(null);
				logger.fine("null value added to the hashset");
			}
			
		}
		return new DctmSysobjectProperty(name, hashSet);

	}

	private Calendar getCalendarFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public Set getPropertyNames() throws RepositoryException {
		logger.fine("fetching the object");
		fetch();
		HashSet properties = new HashSet();

		for (int i = 0; i < object.getAttrCount(); i++) {
			IAttr curAttr = object.getAttr(i);
			String name = curAttr.getName();
			logger.finest("pass the attribute "+name);
			if (!excluded_meta.contains(name) || included_meta.contains(name)) {
				properties.add(name);
				logger.finest("attribute "+name+" added to the properties");
			}
		}
		return properties;
	}

	public Calendar getDate(String name) throws IllegalArgumentException,
			RepositoryException {

		Date date = object.getTime(name).getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
}
