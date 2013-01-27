package gov.hhs.onc.dcdt.utils.beans;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.annotations.ConfigBean;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PrivateKeyFactory;

@ConfigBean("entries/entry")
public class Entry extends ToolBean
{
	public final static String RSA_ALG_NAME = "RSA";
	public final static String PKCS12_KEYSTORE_TYPE = "PKCS12";
	public final static String X509_CERT_TYPE = "X.509";
	
	private final static String CERT_SUFFIX = "_cert";
	private final static String KEY_SUFFIX = "_key";
	
	private final static String DER_EXT = ".der";
	private final static String PEM_EXT = ".pem";
	private final static String PKCS12_EXT = ".p12";
	
	private String name;
	private String path;
	private int keyBits;
	private int validDays;
	private boolean canSign;
	private EntryDestination destination = new EntryDestination();
	private EntryDn dn = new EntryDn();
	private KeyPair keyPair;
	private X509Certificate cert;
	private KeyStore keyStore;
	private Entry issuer;
	
	public String getCertDerFilePath()
	{
		return this.getFilePath(CERT_SUFFIX, DER_EXT);
	}
	
	public String getCertPemFilePath()
	{
		return this.getFilePath(CERT_SUFFIX, PEM_EXT);
	}
	
	public String getKeyDerFilePath()
	{
		return this.getFilePath(KEY_SUFFIX, DER_EXT);
	}
	
	public String getKeyPemFilePath()
	{
		return this.getFilePath(KEY_SUFFIX, PEM_EXT);
	}
	
	public String getKeyStoreFilePath()
	{
		return this.getFilePath(PKCS12_EXT);
	}
	
	private String getFilePath(String ... suffixes)
	{
		StringBuilder fileNameBuilder = new StringBuilder();
		
		if (this.hasPath())
		{
			fileNameBuilder.append(this.getPath());
			fileNameBuilder.append(File.separatorChar);
		}
		
		fileNameBuilder.append(this.getName());
		
		for (String suffix : suffixes)
		{
			fileNameBuilder.append(suffix);
		}
		
		return fileNameBuilder.toString();
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
		builder.append(this.name);
		builder.append(", path=");
		builder.append(this.path);
		builder.append(", keyBits=");
		builder.append(this.keyBits);
		builder.append(", validDays=");
		builder.append(this.validDays);
		
		if (this.hasDestination())
		{
			builder.append(", destination={");
			builder.append(this.destination);
			builder.append("}");
		}
		
		if (this.hasDn())
		{
			builder.append(", dn={");
			builder.append(this.dn);
			builder.append("}");
		}
		
		if (this.hasIssuer())
		{
			Entry issuerEntry = this.issuer;
			
			builder.append(", issuer={name=");
			builder.append(issuerEntry.getName());
			builder.append(", path=");
			builder.append(issuerEntry.getPath());
			builder.append(", dn={");
			builder.append(issuerEntry.getDn());
			builder.append("}}");
		}
		
		return builder.toString();
	}

	public byte[] getCertData() throws CertificateEncodingException
	{
		return this.hasCert() ? this.cert.getEncoded() : null;
	}
	
	public byte[] getKeyData()
	{
		return this.hasPrivateKey() ? this.getPrivateKey().getEncoded() : null;
	}
	
	public byte[] getKeyStoreData() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException
	{
		if (!this.hasKeyStore())
		{
			return null;
		}

		ByteArrayOutputStream keyStoreOutStream = new ByteArrayOutputStream();
		this.keyStore.store(keyStoreOutStream, ArrayUtils.EMPTY_CHAR_ARRAY);
		
		return keyStoreOutStream.toByteArray();
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
		return this.hasPrivateKey() ? PrivateKeyFactory.createKey(this.getKeyData()) : null;
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
		return this.cert != null;
	}
	
	public X509Certificate getCert()
	{
		return this.cert;
	}

	public void setCert(X509Certificate cert)
	{
		this.cert = cert;
	}

	public boolean hasDestination()
	{
		return this.destination != null;
	}
	
	public EntryDestination getDestination()
	{
		return this.destination;
	}

	public void setDestination(EntryDestination destination)
	{
		this.destination = destination;
	}

	public boolean hasDn()
	{
		return this.dn != null;
	}
	
	public EntryDn getDn()
	{
		return this.dn;
	}

	public void setDn(EntryDn dn)
	{
		this.dn = dn;
	}

	public boolean hasIssuer()
	{
		return this.issuer != null;
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
		return (this.keyBits > 0) && (this.keyBits % 8 == 0);
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
		return this.keyPair != null;
	}
	
	public KeyPair getKeyPair()
	{
		return this.keyPair;
	}

	public void setKeyPair(KeyPair keyPair)
	{
		this.keyPair = keyPair;
	}

	public boolean hasKeyStore()
	{
		return this.keyStore != null;
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
		return !StringUtils.isBlank(this.name);
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean hasPath()
	{
		return !StringUtils.isBlank(this.path);
	}

	public String getPath()
	{
		return this.path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public boolean hasValidDays()
	{
		return this.validDays >= 0;
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