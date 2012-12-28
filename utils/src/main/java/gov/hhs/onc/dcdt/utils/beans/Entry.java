package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.utils.annotations.ConfigBean;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

@ConfigBean("entries/entry")
public class Entry
{
	private final static String CERT_SUFFIX = "_cert";
	private final static String KEY_SUFFIX = "_key";
	
	private final static String DER_EXT = ".der";
	private final static String PEM_EXT = ".pem";
	private final static String PKCS12_EXT = ".p12";
	
	private String id;
	private String name;
	private File dir;
	private int keyBits;
	private int validDays;
	private boolean canSign;
	private EntryDn dn = new EntryDn();
	private KeyPair keyPair;
	private X509Certificate cert;
	private KeyStore keyStore;
	private Entry issuer;
	
	public File getCertDerFile()
	{
		return this.getFile(CERT_SUFFIX, DER_EXT);
	}
	
	public File getCertPemFile()
	{
		return this.getFile(CERT_SUFFIX, PEM_EXT);
	}
	
	public File getKeyDerFile()
	{
		return this.getFile(KEY_SUFFIX, DER_EXT);
	}
	
	public File getKeyPemFile()
	{
		return this.getFile(KEY_SUFFIX, PEM_EXT);
	}
	
	public File getKeyStoreFile()
	{
		return this.getFile(PKCS12_EXT);
	}
	
	private File getFile(String ... suffixes)
	{
		StringBuilder fileNameBuilder = new StringBuilder(this.getName());
		
		for (String suffix : suffixes)
		{
			fileNameBuilder.append(suffix);
		}
		
		return new File(this.getDir(), fileNameBuilder.toString());
	}
	
	public boolean isCa()
	{
		return this.getCanSign() && this.hasIssuer() && this.equals(this.getIssuer());
	}
	
	public boolean isLeaf()
	{
		return !this.isCa();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Entry)
		{
			Entry entryCompare = (Entry)obj;
			
			return StringUtils.equals(this.getName(), entryCompare.getName()) && 
				this.hasDn() && entryCompare.hasDn() && this.getDn().equals(entryCompare.getDn()) ;
		}
		
		return false;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("name=");
		builder.append(this.getName());
		builder.append(", dir=");
		builder.append(this.getDir());
		builder.append(", keyBits=");
		builder.append(this.getKeyBits());
		builder.append(", validDays=");
		builder.append(this.getValidDays());
		builder.append(", dn={");
		builder.append(this.getDn().toString());
		builder.append("}");
		builder.append(", issuer={");
		
		if (this.hasIssuer())
		{
			Entry issuerEntry = this.getIssuer();
			
			builder.append("name=");
			builder.append(issuerEntry.getName());
			builder.append(", dir=");
			builder.append(issuerEntry.getDir());
			builder.append(", dn={");
			builder.append(issuerEntry.getDn().toString());
			builder.append("}");
		}
		
		builder.append("}");
		
		return builder.toString();
	}

	public AuthorityKeyIdentifier getAuthKeyId()
	{
		return this.hasIssuer() && this.getIssuer().hasPublicKey() ? 
			new AuthorityKeyIdentifier(this.getIssuer().getPublicKeyInfo()) : null;
	}
	
	public SubjectKeyIdentifier getSubjKeyId()
	{
		if (this.hasPublicKey())
		{
			Digest subjKeyDigest = new SHA1Digest();
			byte[] pubKeyData = this.getPublicKeyInfo().getPublicKeyData().getBytes(), 
				subjKeyIdData = new byte[subjKeyDigest.getDigestSize()];
			
			subjKeyDigest.update(pubKeyData, 0, pubKeyData.length);
			subjKeyDigest.doFinal(subjKeyIdData, 0);
			
			return new SubjectKeyIdentifier(subjKeyIdData);
		}
		
		return null;
	}
	
	public SubjectPublicKeyInfo getPublicKeyInfo()
	{
		return this.hasPublicKey() ? SubjectPublicKeyInfo.getInstance(this.getPublicKey().getEncoded()) : null;
	}
	
	public boolean hasPublicKey()
	{
		return this.getPublicKey() != null;
	}
	
	public PublicKey getPublicKey()
	{
		return this.hasKeyPair() ? this.getKeyPair().getPublic() : null;
	}
	
	public AsymmetricKeyParameter getPrivateKeyParam() throws IOException
	{
		return this.hasPrivateKey() ? PrivateKeyFactory.createKey(this.getPrivateKey().getEncoded()) : null;
	}
	
	public boolean hasPrivateKey()
	{
		return this.getPrivateKey() != null;
	}
	
	public PrivateKey getPrivateKey()
	{
		return this.hasKeyPair() ? this.getKeyPair().getPrivate() : null;
	}
	
	public boolean getCanSign()
	{
		return this.canSign;
	}

	public void setCanSign(boolean canSign)
	{
		this.canSign = canSign;
	}
	
	public boolean hasCert()
	{
		return this.getCert() != null;
	}
	
	public X509Certificate getCert()
	{
		return this.cert;
	}

	public void setCert(X509Certificate cert)
	{
		this.cert = cert;
	}

	public boolean hasDir()
	{
		return this.getDir() != null;
	}
	
	public File getDir()
	{
		return this.dir;
	}

	public void setDir(File dir)
	{
		this.dir = dir;
	}
	
	public boolean hasDn()
	{
		return this.getDn() != null;
	}
	
	public EntryDn getDn()
	{
		return this.dn;
	}

	public void setDn(EntryDn dn)
	{
		this.dn = dn;
	}

	public boolean hasId()
	{
		return !StringUtils.isBlank(this.getId());
	}
	
	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public boolean hasIssuer()
	{
		return this.getIssuer() != null;
	}
	
	public Entry getIssuer()
	{
		return this.issuer;
	}

	public void setIssuer(Entry issuer)
	{
		this.issuer = issuer;
	}

	public boolean hasKeyBits()
	{
		return (this.getKeyBits() > 0) && (this.getKeyBits() % 2 == 0);
	}
	
	public int getKeyBits()
	{
		return this.keyBits;
	}

	public void setKeyBits(int keyBits)
	{
		this.keyBits = keyBits;
	}

	public boolean hasKeyPair()
	{
		return this.getKeyPair() != null;
	}
	
	public KeyPair getKeyPair()
	{
		return this.keyPair;
	}

	public void setKeyPair(KeyPair keyPair)
	{
		this.keyPair = keyPair;
	}

	public KeyStore getKeyStore()
	{
		return this.keyStore;
	}

	public void setKeyStore(KeyStore keyStore)
	{
		this.keyStore = keyStore;
	}

	public boolean hasName()
	{
		return !StringUtils.isBlank(this.getName());
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean hasValidDays()
	{
		return this.getValidDays() >= 0;
	}
	
	public int getValidDays()
	{
		return this.validDays;
	}

	public void setValidDays(int validDays)
	{
		this.validDays = validDays;
	}
}