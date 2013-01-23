package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import gov.hhs.onc.dcdt.utils.ldap.UtilityLdapException;
import java.util.Arrays;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.exception.LdapInvalidDnException;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.url.LdapUrl;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

@ConfigBean("ldaps/ldap")
public class LdapService extends UtilityBean
{
	private static class LdapServiceConnectionConfig extends LdapConnectionConfig
	{
		private long defaultTimeout = super.getDefaultTimeout();
		
		@Override
		public long getDefaultTimeout()
		{
			return this.defaultTimeout;
		}

		public void setDefaultTimeout(long defaultTimeout)
		{
			this.defaultTimeout = defaultTimeout;
		}
	}
	
	private final static String HASH_BIND_PASS_DIGEST_NAME_PREFIX = "{";
	private final static String HASH_BIND_PASS_DIGEST_NAME_SUFFIX = "}";
	private final static LdapSecurityConstants HASH_BIND_PASS_DIGEST_DEFAULT = LdapSecurityConstants.HASH_METHOD_SHA;
	
	private final static long TIMEOUT_DEFAULT = 5000;
	
	private String name;
	private boolean real;
	private String host;
	private int port;
	private boolean useSsl;
	private boolean anonymousBind;
	private Dn bindDn;
	private String bindPass;
	private boolean hashBindPass;
	private LdapSecurityConstants hashBindPassDigest;

	public LdapUrl toUrl()
	{
		return this.toUrl(null, null, null);
	}
	
	public LdapUrl toUrl(Dn baseDn, String filter, SearchScope scope, String ... attributes)
	{
		LdapUrl url = new LdapUrl();
		url.setHost(this.host);
		url.setPort(this.port);
		url.setScheme(this.useSsl ? LdapUrl.LDAPS_SCHEME : LdapUrl.LDAP_SCHEME);
		url.setDn(baseDn);
		url.setFilter(filter);
		url.setScope(scope);
		url.setAttributes(Arrays.asList(attributes));
		
		return url;
	}
	
	public LdapServiceConnectionConfig toConnectionConfig()
	{
		LdapServiceConnectionConfig connectionConfig = new LdapServiceConnectionConfig();
		connectionConfig.setLdapHost(this.host);
		connectionConfig.setLdapPort(this.port);
		connectionConfig.setUseSsl(this.useSsl);
		
		if (!this.anonymousBind)
		{
			connectionConfig.setName(this.getBindDnName());
			connectionConfig.setCredentials(this.getBindPass());
		}
		
		connectionConfig.setDefaultTimeout(TIMEOUT_DEFAULT);
		
		return connectionConfig;
	}
	
	public String getBindDnName()
	{
		return (this.bindDn != null) ? this.bindDn.getName() : null;
	}
	
	public void setBindDnName(String bindDnName) throws UtilityLdapException
	{
		this.setBindDn(parseBindDn(bindDnName));
	}
	
	public String getBindPass()
	{
		return this.hashBindPass ? hashBindPass(this.bindPass, this.hashBindPassDigest) : this.bindPass;
	}
	
	public void setBindPass(String bindPass)
	{
		this.bindPass = bindPass;
	}

	public String getHashBindPassDigestName()
	{
		return (this.hashBindPassDigest != null) ? this.hashBindPassDigest.getName() : null;
	}
	
	public void setHashBindPassDigestName(String digestName) throws UtilityLdapException
	{
		this.hashBindPassDigest = LdapSecurityConstants.getAlgorithm(digestName);
		
		if (this.hashBindPassDigest == null)
		{
			throw new UtilityLdapException("Unknown LDAP hash digest algorithm: " + digestName);
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("name=");
		builder.append(this.name);
		builder.append(", real=");
		builder.append(this.real);
		builder.append(", host=");
		builder.append(this.host);
		builder.append(", port=");
		builder.append(this.port);
		builder.append(", useSsl=");
		builder.append(this.useSsl);
		
		if (this.anonymousBind)
		{
			builder.append(", anonymousBind=");
			builder.append(this.anonymousBind);
		}
		else
		{
			builder.append(", bindDn=");
			builder.append(this.getBindDnName());
			
			// Only output bind pass if it will be hashed
			if (this.hashBindPass)
			{
				builder.append(", bindPass=");
				builder.append(this.getBindPass());
			}
				
			builder.append(", hashBindPass=");
			builder.append(this.hashBindPass);
			builder.append(", hashBindPassDigest=");
			builder.append(this.getHashBindPassDigestName());
		}
		
		return builder.toString();
	}
	
	private static String hashBindPass(String bindPass, LdapSecurityConstants digest)
	{
		String digestName = ObjectUtils.defaultIfNull(digest, HASH_BIND_PASS_DIGEST_DEFAULT).getName();
		
		return HASH_BIND_PASS_DIGEST_NAME_PREFIX + digestName.toUpperCase() + HASH_BIND_PASS_DIGEST_NAME_SUFFIX + 
			Base64.encodeBase64String(DigestUtils.getDigest(digestName).digest(bindPass.getBytes()));
	}

	private static Dn parseBindDn(String bindDnStr) throws UtilityLdapException
	{
		try
		{
			return new Dn(bindDnStr);
		}
		catch (LdapInvalidDnException e)
		{
			throw new UtilityLdapException("Unable to parse LDAP bind distinguished name: " + bindDnStr, e);
		}
	}

	public boolean isAnonymousBind()
	{
		return this.anonymousBind;
	}

	public void setAnonymousBind(boolean anonymousBind)
	{
		this.anonymousBind = anonymousBind;
	}

	public Dn getBindDn()
	{
		return this.bindDn;
	}

	public void setBindDn(Dn bindDn)
	{
		this.bindDn = bindDn;
	}

	public boolean hashBindPass()
	{
		return this.hashBindPass;
	}

	public void setHashBindPass(boolean hashBindPass)
	{
		this.hashBindPass = hashBindPass;
	}
	
	public LdapSecurityConstants getHashBindPassDigest()
	{
		return this.hashBindPassDigest;
	}

	public void setHashBindPassDigest(LdapSecurityConstants hashBindPassDigest)
	{
		this.hashBindPassDigest = hashBindPassDigest;
	}

	public boolean isReal()
	{
		return this.real;
	}

	public void setReal(boolean real)
	{
		this.real = real;
	}

	public String getHost()
	{
		return this.host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPort()
	{
		return this.port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public boolean getUseSsl()
	{
		return this.useSsl;
	}

	public void setUseSsl(boolean useSsl)
	{
		this.useSsl = useSsl;
	}
}