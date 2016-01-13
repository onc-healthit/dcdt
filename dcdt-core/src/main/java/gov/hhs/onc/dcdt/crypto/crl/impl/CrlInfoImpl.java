package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateDnImpl;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryInfo;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlInfo;
import gov.hhs.onc.dcdt.crypto.crl.CrlType;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509Crl;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509CrlEntry;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Date;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.TBSCertList.CRLEntry;

public class CrlInfoImpl extends AbstractCrlDescriptor<CrlEntryInfo> implements CrlInfo {
    private ToolX509Crl crl;
    private Date nextUpdate;
    private Date thisUpdate;

    public CrlInfoImpl() throws CrlException {
        this(null);
    }

    public CrlInfoImpl(@Nullable ToolX509Crl crl) throws CrlException {
        this.setCrl(crl);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        CrlInfo crlInfo;

        return (this.hasCrl() && (obj != null) && ToolClassUtils.isAssignable(obj.getClass(), CrlInfo.class) && (crlInfo = ((CrlInfo) obj)).hasCrl() && this.crl
            .equals(crlInfo.getCrl()));
    }

    @Override
    public int hashCode() {
        return (this.hasCrl() ? this.crl.hashCode() : super.hashCode());
    }

    @Override
    protected void reset() {
        super.reset();

        this.nextUpdate = null;
        this.thisUpdate = null;
    }

    private void processCrl() throws CrlException {
        this.reset();

        if (!this.hasCrl()) {
            return;
        }

        CRLEntry[] crlEntries = this.crl.getCertificateList().getRevokedCertificates();
        CrlEntryInfo crlEntryInfo;

        for (CRLEntry crlEntry : crlEntries) {
            // noinspection ConstantConditions
            this.entries.put((crlEntryInfo = new CrlEntryInfoImpl(new ToolX509CrlEntry(crlEntry))).getSerialNumber(), crlEntryInfo);
        }

        try {
            this.crlType = CrlType.X509;
            this.issuerDn = new CertificateDnImpl(X500Name.getInstance(BCStyle.INSTANCE, this.crl.getIssuerX500Principal().getEncoded()));
            // noinspection ConstantConditions
            this.num =
                (this.hasExtension(Extension.cRLNumber) ? ASN1Integer.getInstance(this.getExtension(Extension.cRLNumber).getParsedValue()).getValue() : null);
            this.nextUpdate = this.crl.getNextUpdate();
            this.sigAlg = CryptographyUtils.findByOid(SignatureAlgorithm.class, new ASN1ObjectIdentifier(this.crl.getSigAlgOID()));
            this.thisUpdate = this.crl.getThisUpdate();
        } catch (Exception e) {
            this.reset();

            throw e;
        }
    }

    @Override
    public boolean hasCrl() {
        return (this.crl != null);
    }

    @Nullable
    @Override
    public ToolX509Crl getCrl() {
        return this.crl;
    }

    @Override
    public void setCrl(@Nullable ToolX509Crl crl) throws CrlException {
        this.crl = crl;

        this.processCrl();
    }

    @Override
    public boolean hasExtension(ASN1ObjectIdentifier oid) {
        return (this.getExtension(oid) != null);
    }

    @Nullable
    @Override
    public Extension getExtension(ASN1ObjectIdentifier oid) {
        // noinspection ConstantConditions
        return (this.hasExtensions() ? this.getExtensions().getExtension(oid) : null);
    }

    @Override
    public boolean hasExtensions() {
        return (this.getExtensions() != null);
    }

    @Nullable
    @Override
    public Extensions getExtensions() {
        return (this.hasCrl() ? this.crl.getCertificateList().getTBSCertList().getExtensions() : null);
    }

    @Override
    public boolean hasNextUpdate() {
        return (this.nextUpdate != null);
    }

    @Nullable
    @Override
    public Date getNextUpdate() {
        return this.nextUpdate;
    }

    @Override
    public boolean hasThisUpdate() {
        return (this.thisUpdate != null);
    }

    @Nullable
    @Override
    public Date getThisUpdate() {
        return thisUpdate;
    }
}
