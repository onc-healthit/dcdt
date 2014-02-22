package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithm;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.CertRecordConfig;
import javax.annotation.Nonnegative;
import org.xbill.DNS.CERTRecord;

public class CertRecordConfigImpl extends AbstractDnsRecordConfig<CERTRecord> implements CertRecordConfig {
    private byte[] certData;
    private DnsCertificateType certType;
    private DnsKeyAlgorithm keyAlg;
    private int keyTag;

    public CertRecordConfigImpl() {
        super(DnsRecordType.CERT, CERTRecord.class);
    }

    @Override
    public CERTRecord toRecord() throws DnsException {
        return new CERTRecord(this.name, this.recordType.getDclass(), this.ttl, this.certType.getType(), this.keyTag, this.keyAlg.getAlgorithm(), this.certData);
    }

    @Override
    public byte[] getCertificateData() {
        return this.certData;
    }

    @Override
    public void setCertificateData(byte[] certData) {
        this.certData = certData;
    }

    @Override
    public DnsCertificateType getCertificateType() {
        return this.certType;
    }

    @Override
    public void setCertificateType(DnsCertificateType certType) {
        this.certType = certType;
    }

    @Override
    public DnsKeyAlgorithm getKeyAlgorithm() {
        return this.keyAlg;
    }

    @Override
    public void setKeyAlgorithm(DnsKeyAlgorithm keyAlg) {
        this.keyAlg = keyAlg;
    }

    @Nonnegative
    @Override
    public int getKeyTag() {
        return this.keyTag;
    }

    @Override
    public void setKeyTag(@Nonnegative int keyTag) {
        this.keyTag = keyTag;
    }
}
