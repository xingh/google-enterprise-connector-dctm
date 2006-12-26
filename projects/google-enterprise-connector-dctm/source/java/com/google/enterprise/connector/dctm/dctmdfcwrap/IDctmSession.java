package com.google.enterprise.connector.dctm.dctmdfcwrap;


import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.IDfId;
import com.documentum.fc.common.IDfLoginInfo;
import com.google.enterprise.connector.dctm.dfcwrap.IId;
import com.google.enterprise.connector.dctm.dfcwrap.ILoginInfo;
import com.google.enterprise.connector.dctm.dfcwrap.ISession;
import com.google.enterprise.connector.dctm.dfcwrap.ISysObject;
import com.google.enterprise.connector.spi.LoginException;

public class IDctmSession implements ISession{
	//IDctmSession idm=new IDfSession();
	IDfSession idfSession;
	
	
	public IDctmSession(IDfSession DfSession){
		this.idfSession=DfSession;
	}
	
	
	public String getSessionId(){
		String IDSess=null;
		try{
			IDSess=idfSession.getSessionId();
		}catch(DfException de){
			de.getMessage();
		}
		return IDSess;
		
	}
	
	public ISysObject getObject(IId objectId){
		if (!(objectId instanceof IDctmId)) {
			throw new IllegalArgumentException();
		}
		IDctmId dctmId = (IDctmId) objectId;
		IDfId idfId=dctmId.getidfId();
		IDfSysObject idfSysObject=null;
		try{
			idfSysObject=(IDfSysObject)idfSession.getObject(idfId);
		}catch(DfException de){
			de.getMessage();
		}
		return new IDctmSysObject(idfSysObject);
	}
	
	
	public ISysObject getObjectByQualification(String qualification){
		IDfSysObject idfSysObject=null;
		try{
			idfSysObject=(IDfSysObject)idfSession.getObjectByQualification(qualification);
		}catch(DfException de){
			de.getMessage();
		}
		return new IDctmSysObject(idfSysObject);
	}
	
//	public void authenticate(ILoginInfo loginInfo){
//		if (!(loginInfo instanceof IDctmLoginInfo)) {
//			throw new IllegalArgumentException();
//		}
//		IDctmLoginInfo dctmLoginInfo = (IDctmLoginInfo) loginInfo;
//		IDfLoginInfo idfLoginInfo=dctmLoginInfo.getIdfLoginInfo();
//		try{
//			idfSession.authenticate(idfLoginInfo);
//		}catch(DfException de){
//			try {
//				throw new LoginException(de.getMessage());
//			} catch (LoginException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

	public IDfSession getDfSession() {
		return idfSession;
	}
	
	
	public void setDfSession(IDfSession dfSession) {
		idfSession = dfSession;
	}


	public String getLoginTicketForUser(String username) {
		String ticket = null;
		try {
			ticket = this.idfSession.getLoginTicketForUser(username);
		} catch (DfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ticket;
	}


	public String getDocbaseName() {
		String docbaseName = null;
		try {
			docbaseName = this.idfSession.getDocbaseName();
		} catch (DfException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return docbaseName;
	}
	
    
	
	/*
	public IDctmSession(String docbase){
		client = DfClient.getLocalClientEx();
	  	//.getClientNetworkLocations
		IDfSessionManager sMgr = client.newSessionManager(); 
		try{
		IDfSession myses=sMgr.getSession(docbase);
		}catch(DfAuthenticationException dae){
			dae.getMessage();
		}catch(DfIdentityException di){
			di.getMessage();
		}catch(DfPrincipalException dfp){
			dfp.getMessage();
		}catch(DfServiceException dfse){
			dfse.getMessage();
		}
	}
	*/
	
	
	
	/*
	void 	addDynamicGroup(String groupName);
	boolean 	apiExec(String cmd, String args); 
	IDfCollection 	apply(String objId, String functionName, IDfList args, IDfList dataType, IDfList values);
	IDfId 	archive(String predicate, String operatorName, int priority, boolean sendMail, IDfTime dueDate); 
	void 	assume(IDfLoginInfo loginInfo);
	void 	authenticate(IDfLoginInfo loginInfo);
	void 	beginTrans();
     
void 	changePassword(String oldPasswd, String newPasswd);
    
void 	commitTrans();
    
void 	dequeue(IDfId stampId);
    
String 	describe(String type, String objType);
    
void 	disconnect();

String 	exportTicketKey(String password);
    
void 	flush(String flushType, String cacheKey);
void 	flushCache(boolean discardChanged);

IDfACL 	getACL(String aclDomain, String aclName);
IDfAcsTransferPreferences 	getAcsTransferPreferences();

String 	getAliasSet();
String 	getApplicationToken(String userName, String scope, int timeout, String appId, boolean machine_only);
IDfAuditTrailManager 	getAuditTrailManager();

IDfClient 	getClient();

IDfTypedObject 	getClientConfig();

IDfTypedObject 	getConnectionConfig();

String 	getDBMSName();
   
int 	getDefaultACL();
   
String 	getDMCLSessionId();
   
IDfTypedObject 	getDocbaseConfig();
     
String 	getDocbaseId();
   
String 	getDocbaseName();
    
String 	getDocbaseOwnerName();
    
String 	getDocbaseScope();
  
IDfTypedObject 	getDocbrokerMap();
    
String 	getDynamicGroup(int index);
     
int 	getDynamicGroupCount();
   
IDfCollection 	getEvents();
 
IDfFolder 	getFolderByPath(String folderPath);
    
IDfFormat 	getFormat(String formatName);
    
IDfGroup 	getGroup(String groupName);
     
IDfId 	getIdByQualification(String qualification);
    
IDfCollection 	getLastCollection();
   
IDfLoginInfo 	getLoginInfo();
    
String 	getLoginTicket();
    
String 	getLoginTicketEx(String userName, String scope, int timeout, boolean single_use, String serverName);
    
String 	getLoginTicketForUser(String userName);

String 	getLoginUserName();

String 	getMessage(int severityLevel);
     
IDfLocalModuleRegistry 	getModuleRegistry();
   
IDfPersistentObject 	getObject(IDfId objectId);
     IDfPersistentObject 	getObjectByPath(String objectPath);

IDfPersistentObject 	getObjectByQualification(String qualification);

IDfPersistentObject 	getObjectByQualificationWithInterface(String qualification, String interfaceName);
     
IDfEnumeration 	getObjectPaths(IDfId objectId);
    
IDfPersistentObject 	getObjectWithCaching(IDfId objectId, String typeName, String className, String currencyCheckValue, boolean usePersistentCache, boolean useSharedCache);
    
IDfPersistentObject 	getObjectWithInterface(IDfId objectId, String interfaceName);
    
IDfPersistentObject 	getObjectWithType(IDfId objectId, String typeName, String className);
 
IDfRelationType 	getRelationType(String relationName);
    
IDfCollection 	getRunnableProcesses(String additionalAttributes);
    
String 	getSecurityMode();
     
IDfTypedObject 	getServerConfig();
    
IDfTypedObject 	getServerMap(String docbaseName);
     
String 	getServerVersion();
     
IDfTypedObject 	getSessionConfig();

String 	getSessionId();
    
IDfSessionManager 	getSessionManager();
     
IDfCollection 	getTasks(String userName, int filter, String additionalAttributes, String orderBy);

IDfCollection 	getTasksEx(String userName, int filter, IDfList orderByList, IDfList ascendingList);
     
IDfType 	getType(String typeName);

IDfTypedObject 	getTypeDescription(String typeName, String attribute, IDfId businessPolicyId, String state);
   
IDfUser 	getUser(String userName);
     
IDfUser 	getUserByOSName(String userOSName, String userOSDomain);
    
IDfVersionTreeLabels 	getVersionTreeLabels(IDfId chronicleId);
  
boolean 	hasEvents();
     
boolean 	importTicketKey(String key, String password);
     
boolean 	isACLDocbase();
   
boolean 	isAdopted();
     
boolean 	isConnected();
    
boolean 	isRemote();
    
boolean 	isShared();
    
boolean 	isTransactionActive();
     
boolean 	lock(int timeoutInMsec);
     
IDfPersistentObject 	newObject(String typeName);
     
IDfPersistentObject 	newObjectWithType(String typeName, String className);
    
IDfWorkflowBuilder 	newWorkflowBuilder(IDfId processId);

void 	purgeLocalFiles();
    
void 	reInit(String serverConfigName);
    
void 	removeDynamicGroup(String groupName);
    
boolean 	resetPassword(String password);
    
boolean 	resetTicketKey();
    
String 	resolveAlias(IDfId sysObject, String scopeAlias);

void 	reStart(String serverConfigName, boolean restartClient);
    
IDfId 	restore(String predicate, String dumpFile, String operatorName, int priority, boolean sendMail, IDfTime dueDate);

IDfId 	sendToDistributionList(IDfList toUsers, IDfList toGroups, String instructions, IDfList objectIDs, int priority, boolean endNotification);
    
IDfId 	sendToDistributionListEx(IDfList toUsers, IDfList toGroups, String instructions, IDfList objectIDs, int priority, int flags);

void 	setAcsTransferPreferences(IDfAcsTransferPreferences acsTransferPreferences);

void 	setAliasSet(String name);

void 	setBatchHint(int batchSize);
    
String 	setDocbaseScope(String docbaseName);
    
String 	setDocbaseScopeById(IDfId objectId);
   
void 	shutdown(boolean immediate, boolean deleteEntry);

void 	traceDMCL(int level, String traceFile);

boolean 	unlock();
*/
}
