package gov.hhs.onc.dcdt.dns.config;

import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import javax.annotation.Nonnegative;
import org.xbill.DNS.CERTRecord;

public interface CertRecordConfig extends DnsRecordConfig<CERTRecord> {
    public byte[] getCertificateData();

    public void setCertificateData(byte[] certData);

    public DnsCertificateType getCertificateType();

    public void setCertificateType(DnsCertificateType certType);

    public DnsKeyAlgorithmType getKeyAlgorithmType();

    public void setKeyAlgorithmType(DnsKeyAlgorithmType keyAlgType);

    @Nonnegative
    public int getKeyTag();

    public void setKeyTag(@Nonnegative int keyTag);
}
