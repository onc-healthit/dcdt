package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.CryptographyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CrlUtils.ToolX509CrlEntry;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;

public interface CrlEntryInfo extends CrlEntryDescriptor, CryptographyInfo {
    public boolean hasCrlEntry();

    @Nullable
    public ToolX509CrlEntry getCrlEntry();

    public void setCrlEntry(@Nullable ToolX509CrlEntry crlEntry) throws CrlException;

    public boolean hasExtension(ASN1ObjectIdentifier oid);

    @Nullable
    public Extension getExtension(ASN1ObjectIdentifier oid);

    public boolean hasExtensions();

    @Nullable
    public Extensions getExtensions();
}
