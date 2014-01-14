package gov.hhs.onc.dcdt.testcases.discovery;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseCredential extends ToolBean {
    public boolean hasCertificateData();

    @Nullable
    public byte[] getCertificateData() throws CryptographyException;

    public void setCertificateData(@Nullable byte[] certData) throws CryptographyException;

    public boolean hasCredentialConfig();

    @Nullable
    public CredentialConfig getCredentialConfig();

    public void setCredentialConfig(@Nullable CredentialConfig credConfig);

    public boolean hasCredentialInfo();

    @Nullable
    public CredentialInfo getCredentialInfo();

    public void setCredentialInfo(@Nullable CredentialInfo credInfo);

    public boolean hasIssuerCredential();

    @Nullable
    public DiscoveryTestcaseCredential getIssuerCredential();

    public void setIssuerCredential(@Nullable DiscoveryTestcaseCredential issuerCred);

    public boolean hasName();

    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    public boolean hasPrivateKeyData();

    @Nullable
    public byte[] getPrivateKeyData() throws CryptographyException;

    public void setPrivateKeyData(@Nullable byte[] privateKeyData) throws CryptographyException;
}
