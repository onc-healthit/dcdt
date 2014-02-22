package gov.hhs.onc.dcdt.dns.config;

import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithm;
import javax.annotation.Nonnegative;
import org.xbill.DNS.CERTRecord;

public interface CertRecordConfig extends DnsRecordConfig<CERTRecord> {
    public byte[] getCertificateData();

    public void setCertificateData(byte[] cert);

    public DnsCertificateType getCertificateType();

    public void setCertificateType(DnsCertificateType certType);

    public DnsKeyAlgorithm getKeyAlgorithm();

    public void setKeyAlgorithm(DnsKeyAlgorithm alg);

    @Nonnegative
    public int getKeyTag();

    public void setKeyTag(@Nonnegative int keyTag);
}
