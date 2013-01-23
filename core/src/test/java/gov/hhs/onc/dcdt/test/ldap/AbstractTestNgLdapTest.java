package gov.hhs.onc.dcdt.test.ldap;

import java.util.UUID;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.core.annotations.CreateDS;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DSAnnotationProcessor;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.apache.directory.server.factory.ServerAnnotationProcessor;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.Transport;

public abstract class AbstractTestNgLdapTest
{
	protected final static String APACHEDS_WORK_DIR_SYS_PROP_NAME = "workingDirectory";
	protected final static String DCDT_TEST_LDAP_WORK_DIR_SYS_PROP_NAME = "dcdt.test.ldap.work.dir";
	
	protected DirectoryServiceFactory dirServiceFactory;
	protected DirectoryService dirService;
	protected LdapServer ldapServer;
	
	public boolean startLdapServer() throws Exception
	{
		if (!this.hasLdapServer() || (this.isLdapServerStarted() && !this.stopLdapServer()))
		{
			return false;
		}
		
		this.ldapServer.start();
		
		return this.isLdapServerStarted();
	}
	
	public boolean stopLdapServer() throws Exception
	{
		if (!this.hasLdapServer() || !this.isLdapServerStarted())
		{
			return true;
		}
		
		this.ldapServer.stop();
		
		return !this.isLdapServerStarted();
	}
	
	public boolean isLdapServerStarted()
	{
		return this.hasLdapServer() && this.ldapServer.isStarted();
	}
	
	protected static void initClassAnnotations(AbstractTestNgLdapTest testClassInstance) throws Exception
	{
		Class<? extends AbstractTestNgLdapTest> testClass = testClassInstance.getClass();
		
		DirectoryServiceFactory dirServiceFactory = testClassInstance.getDirServiceFactory();
		testClassInstance.setDirServiceFactory(dirServiceFactory);
		
		CreateDS createDirServiceAnno = testClass.getAnnotation(CreateDS.class);
		DirectoryService dirService = (createDirServiceAnno != null) ? createDirService(dirServiceFactory, createDirServiceAnno) : 
			createDirService(dirServiceFactory, testClass.getName());
		testClassInstance.setDirService(dirService);
		
		CreateLdapServer createLdapServerAnno = testClass.getAnnotation(CreateLdapServer.class);
		LdapServer ldapServer = (createLdapServerAnno != null) ? createLdapServer(dirService, createLdapServerAnno) : 
			createLdapServer(dirService);
		testClassInstance.setLdapServer(ldapServer);
	}
	
	protected static LdapServer createLdapServer(DirectoryService dirService, CreateLdapServer createLdapServerAnno)
	{
		if (createLdapServerAnno == null)
		{
			return null;
		}
		
		return ServerAnnotationProcessor.instantiateLdapServer(createLdapServerAnno, dirService);
	}
	
	protected static LdapServer createLdapServer(DirectoryService dirService, Transport ... ldapServerTransports)
	{
		LdapServer ldapServer = new LdapServer();
		ldapServer.setDirectoryService(dirService);
		ldapServer.setTransports(ldapServerTransports);
		
		return ldapServer;
	}
	
	protected static DirectoryService createDirService(DirectoryServiceFactory dirServiceFactory, CreateDS createDirServiceAnno)
		throws Exception
	{
		if (createDirServiceAnno == null)
		{
			return null;
		}
		
		DirectoryService dirService = DSAnnotationProcessor.createDS(createDirServiceAnno);
		initDirService(dirService);
		initDirServiceFactory(dirServiceFactory, createDirServiceAnno.name());
		
		return dirService;
	}
	
	protected static DirectoryService createDirService(DirectoryServiceFactory dirServiceFactory, String name) throws Exception
	{
		DirectoryService dirService = dirServiceFactory.getDirectoryService();
		initDirService(dirService);
		initDirServiceFactory(dirServiceFactory, name);
		
		return dirService;
	}
	
	protected static void initDirService(DirectoryService dirService)
	{
		if (dirService == null)
		{
			return;
		}
		
		dirService.getChangeLog().setEnabled(true);
	}
	
	protected static DirectoryServiceFactory createDirServiceFactory(String name) throws Exception
	{
		System.setProperty(APACHEDS_WORK_DIR_SYS_PROP_NAME, 
			System.getProperty(DCDT_TEST_LDAP_WORK_DIR_SYS_PROP_NAME));
		
		DirectoryServiceFactory dirServiceFactory = new DefaultDirectoryServiceFactory();
		initDirServiceFactory(dirServiceFactory, name);
		
		System.getProperties().remove(APACHEDS_WORK_DIR_SYS_PROP_NAME);
		
		return dirServiceFactory;
	}
	
	protected static void initDirServiceFactory(DirectoryServiceFactory dirServiceFactory, String name) throws Exception
	{
		if (dirServiceFactory == null)
		{
			return;
		}
		
		dirServiceFactory.init(name + UUID.randomUUID());
	}
	
	protected void initClassAnnotations() throws Exception
	{
		initClassAnnotations(this);
	}
	
	public boolean hasDirService()
	{
		return this.dirService != null;
	}
	
	public DirectoryService getDirService()
	{
		return this.dirService;
	}

	public void setDirService(DirectoryService dirService)
	{
		this.dirService = dirService;
	}

	public boolean hasDirServiceFactory()
	{
		return this.dirServiceFactory != null;
	}
	
	public DirectoryServiceFactory getDirServiceFactory()
	{
		return this.dirServiceFactory;
	}

	public void setDirServiceFactory(DirectoryServiceFactory dirServiceFactory)
	{
		this.dirServiceFactory = dirServiceFactory;
	}

	public boolean hasLdapServer()
	{
		return this.ldapServer != null;
	}
	
	public LdapServer getLdapServer()
	{
		return this.ldapServer;
	}

	public void setLdapServer(LdapServer ldapServer)
	{
		this.ldapServer = ldapServer;
	}
}