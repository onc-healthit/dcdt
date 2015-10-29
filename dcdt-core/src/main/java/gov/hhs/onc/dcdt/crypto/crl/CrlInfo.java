package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509Crl;
import java.util.Date;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public interface CrlInfo extends CrlDescriptor<CrlEntryInfo>, CryptographyInfo {
    public boolean hasCrl();

    @Nullable
    public ToolX509Crl getCrl();

    public void setCrl(@Nullable ToolX509Crl crl) throws CrlException;

    public boolean hasExtension(ASN1ObjectIdentifier oid);

    @Nullable
    public Extension getExtension(ASN1ObjectIdentifier oid);

    public boolean hasExtensions();

    @Nullable
    public Extensions getExtensions();

    public boolean hasNextUpdate();

    @Nullable
    public Date getNextUpdate();

    public boolean hasThisUpdate();

    @Nullable
    public Date getThisUpdate();
}
