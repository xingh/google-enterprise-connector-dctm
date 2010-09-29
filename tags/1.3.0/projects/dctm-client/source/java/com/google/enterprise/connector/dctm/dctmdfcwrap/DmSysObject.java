package com.google.enterprise.connector.dctm.dctmdfcwrap;

import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

import com.documentum.fc.client.IDfFormat;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfAttr;
import com.google.enterprise.connector.dctm.dfcwrap.IAttr;
import com.google.enterprise.connector.dctm.dfcwrap.IFormat;
import com.google.enterprise.connector.dctm.dfcwrap.IId;
import com.google.enterprise.connector.dctm.dfcwrap.ISessionManager;
import com.google.enterprise.connector.dctm.dfcwrap.ISysObject;
import com.google.enterprise.connector.dctm.dfcwrap.ITime;
import com.google.enterprise.connector.dctm.dfcwrap.IValue;
import com.google.enterprise.connector.spi.RepositoryDocumentException;
import com.google.enterprise.connector.spi.RepositoryException;

public class DmSysObject implements ISysObject {

	IDfSysObject idfSysObject;

	private static Logger logger = Logger.getLogger(DmSysObject.class
			.getName());
	
	public DmSysObject(IDfSysObject idfSysObject) {
		this.idfSysObject = idfSysObject;
	}

	public IFormat getFormat() throws RepositoryDocumentException {

		IDfFormat idfFormat = null;

		try {

			idfFormat = idfSysObject.getFormat();

		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}

		return new DmFormat(idfFormat);
	}

	public long getContentSize() throws RepositoryDocumentException {
		long contentSize = 0;
		try {
			contentSize = idfSysObject.getContentSize();
		
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
		return contentSize;

	}

	public ByteArrayInputStream getContent() throws RepositoryDocumentException {
		ByteArrayInputStream content = null;

		try {
			content = idfSysObject.getContent();
			return content;
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
		
	}

	public String getACLDomain() throws RepositoryDocumentException {
		try {
			return idfSysObject.getACLDomain();
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}

	}

	public String getACLName() throws RepositoryDocumentException {
		try {
			return idfSysObject.getACLName();
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public String getString(String name) throws RepositoryDocumentException {
		try {
			if (name.equals("r_object_id") || name.equals("i_chronicle_id")) {
				return idfSysObject.getString(name);
			}
			if (idfSysObject.getAttrDataType(name) == IDfAttr.DM_TIME) {
				return this.getTime(name).getDate().toString();
			} else if (idfSysObject.getAttrDataType(name) == IDfAttr.DM_ID) {
				return this.getId(name).toString();
			}
			return idfSysObject.getString(name);
		} catch (DfException e) {
			// if the attribute name does not exist for the type
			if (e.getMessage().indexOf("DM_API_E_BADATTRNAME") != -1) {
				logger.finest("in the case of DM_API_E_BADATTRNAME");
				return "";
			}
			throw new RepositoryDocumentException(e);
		}

	}

	public boolean getBoolean(String name) throws RepositoryDocumentException {
		try {
			return idfSysObject.getBoolean(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}

	}

	public double getDouble(String name) throws RepositoryDocumentException {

		try {
			return idfSysObject.getDouble(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public IId getId(String name) throws RepositoryDocumentException {

		try {
			return new DmId(idfSysObject.getId(name));
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public int getInt(String name) throws RepositoryDocumentException {

		try {
			return idfSysObject.getInt(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public ITime getTime(String name) throws RepositoryDocumentException {
		try {
			return new DmTime(idfSysObject.getTime(name));
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public int getAttrDataType(String name) throws RepositoryDocumentException {

		try {
			return idfSysObject.getAttrDataType(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public int getAttrCount() throws RepositoryDocumentException {

		try {
			return idfSysObject.getAttrCount();
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public IAttr getAttr(int attrIndex) throws RepositoryDocumentException {

		try {
			return new DmAttr(idfSysObject.getAttr(attrIndex));
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public void setSessionManager(ISessionManager sessionManager)
			throws RepositoryDocumentException {

		DmSessionManager dmSessionManager = (DmSessionManager) sessionManager;
		try {
			this.idfSysObject.setSessionManager(dmSessionManager
					.getSessionManager());
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}

	}

	public IValue getRepeatingValue(String name, int index)
			throws RepositoryDocumentException {
		try {
			return new DmValue(idfSysObject.getRepeatingValue(name, index));
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}

	}

	public int findAttrIndex(String name) throws RepositoryDocumentException {
		try {
			return idfSysObject.findAttrIndex(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

	public int getValueCount(String name) throws RepositoryDocumentException {

		try {
			return idfSysObject.getValueCount(name);
		} catch (DfException e) {
			throw new RepositoryDocumentException(e);
		}
	}

}