package gov.hhs.onc.dcdt.dns.config.impl;

import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsCertificateType;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.CertRecordConfig;
import javax.annotation.Nonnegative;
import org.xbill.DNS.CERTRecord;

public class CertRecordConfigImpl extends AbstractDnsRecordConfig<CERTRecord> implements CertRecordConfig {
    private byte[] certData;
    private DnsCertificateType certType;
    private DnsKeyAlgorithmType keyAlgType;
    private int keyTag;

    public CertRecordConfigImpl() {
        super(DnsRecordType.CERT, CERTRecord.class);
    }

    @Override
    public CERTRecord toRecord() throws DnsException {
        return new CERTRecord(this.name, this.recordType.getDclassType().getType(), this.ttl, this.certType.getTag(), this.keyTag, this.keyAlgType.getTag(),
            this.certData);
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
    public DnsKeyAlgorithmType getKeyAlgorithmType() {
        return this.keyAlgType;
    }

    @Override
    public void setKeyAlgorithmType(DnsKeyAlgorithmType keyAlgType) {
        this.keyAlgType = keyAlgType;
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
