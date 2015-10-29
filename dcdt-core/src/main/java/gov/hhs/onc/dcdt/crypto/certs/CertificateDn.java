package gov.hhs.onc.dcdt.crypto.certs;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.mail.MailAddress;
import java.util.Map;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;

public interface CertificateDn extends ToolBean {
    public X500Name toX500Name();

    public X500Name toX500Name(boolean ordered);

    public Map<ASN1ObjectIdentifier, ASN1Encodable> getAttributes();

    public boolean hasCommonName();

    @Nullable
    public String getCommonName();

    public void setCommonName(@Nullable String commonName);

    public boolean hasMailAddress();

    @Nullable
    public MailAddress getMailAddress();

    public void setMailAddress(@Nullable MailAddress mailAddr);
}
