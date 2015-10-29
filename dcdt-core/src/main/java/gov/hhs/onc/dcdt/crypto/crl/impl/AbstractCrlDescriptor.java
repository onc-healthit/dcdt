package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.crl.CrlDescriptor;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryDescriptor;
import gov.hhs.onc.dcdt.crypto.crl.CrlType;
import gov.hhs.onc.dcdt.crypto.impl.AbstractCryptographyDescriptor;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;

public abstract class AbstractCrlDescriptor<T extends CrlEntryDescriptor> extends AbstractCryptographyDescriptor implements CrlDescriptor<T> {
    protected CrlType crlType;
    protected Map<BigInteger, T> entries = new TreeMap<>();
    protected CertificateDn issuerDn;
    protected SignatureAlgorithm sigAlg;

    @Override
    protected void reset() {
        this.crlType = null;
        this.entries.clear();
        this.issuerDn = null;
        this.sigAlg = null;
    }

    @Override
    public boolean hasCrlType() {
        return (this.crlType != null);
    }

    @Nullable
    @Override
    public CrlType getCrlType() {
        return this.crlType;
    }

    @Override
    public boolean hasEntries() {
        return !this.entries.isEmpty();
    }

    @Override
    public Map<BigInteger, T> getEntries() {
        return this.entries;
    }

    @Override
    public boolean hasIssuerDn() {
        return (this.issuerDn != null);
    }

    @Nullable
    @Override
    public CertificateDn getIssuerDn() {
        return this.issuerDn;
    }

    @Override
    public boolean hasSignatureAlgorithm() {
        return (this.sigAlg != null);
    }

    @Nullable
    @Override
    public SignatureAlgorithm getSignatureAlgorithm() {
        return this.sigAlg;
    }
}
