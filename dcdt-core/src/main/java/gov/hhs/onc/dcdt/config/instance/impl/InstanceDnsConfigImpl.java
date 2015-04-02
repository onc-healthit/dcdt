package gov.hhs.onc.dcdt.config.instance.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDomainAddressBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.ToolPredicate;
import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceDnsConfig;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.dns.DnsKeyAlgorithmType;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.config.ARecordConfig;
import gov.hhs.onc.dcdt.dns.config.CertRecordConfig;
import gov.hhs.onc.dcdt.dns.config.CnameRecordConfig;
import gov.hhs.onc.dcdt.dns.config.DnsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.MxRecordConfig;
import gov.hhs.onc.dcdt.dns.config.NsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.PtrRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SoaRecordConfig;
import gov.hhs.onc.dcdt.dns.config.SrvRecordConfig;
import gov.hhs.onc.dcdt.dns.config.TargetedDnsRecordConfig;
import gov.hhs.onc.dcdt.dns.config.TxtRecordConfig;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordUtils;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;
import org.xbill.DNS.ReverseMap;

@JsonTypeName("instanceDnsConfig")
public class InstanceDnsConfigImpl extends AbstractToolDomainAddressBean implements InstanceDnsConfig {
    private PtrRecordConfig transformReverseMapPtrRecordConfig(ARecordConfig aRecordConfig) {
        PtrRecordConfig ptrRecordConfig = ToolBeanFactoryUtils.createBeanOfType(InstanceDnsConfigImpl.this.appContext, PtrRecordConfig.class);
        // noinspection ConstantConditions
        ptrRecordConfig.setName(ReverseMap.fromAddress(aRecordConfig.getAddress()));
        ptrRecordConfig.setTarget(aRecordConfig.getName());

        return ptrRecordConfig;
    }

    private CertRecordConfig transformDiscoveryTestcaseCredentialCertRecordConfig(DiscoveryTestcaseCredential discoveryTestcaseCred) throws Exception {
        CertRecordConfig certRecordConfig = ToolBeanFactoryUtils.createBeanOfType(InstanceDnsConfigImpl.this.appContext, CertRecordConfig.class);
        // noinspection ConstantConditions
        certRecordConfig.setName(ToolDnsNameUtils.toAbsolute(discoveryTestcaseCred.getLocation().getMailAddress().toAddressName()));

        // noinspection ConstantConditions
        KeyInfo discoveryTestcaseCredKeyInfo = discoveryTestcaseCred.getCredentialInfo().getKeyDescriptor();
        CertificateInfo discoveryTestcaseCredCertInfo = discoveryTestcaseCred.getCredentialInfo().getCertificateDescriptor();

        // noinspection ConstantConditions
        DnsKeyAlgorithmType discoveryTestcaseCredKeyAlgType =
            ToolEnumUtils.findByPredicate(DnsKeyAlgorithmType.class, sigAlgEnum -> Objects.equals(sigAlgEnum.getSignatureAlgorithm(),
                discoveryTestcaseCredCertInfo.getSignatureAlgorithm()));
        // noinspection ConstantConditions
        certRecordConfig.setKeyAlgorithmType(discoveryTestcaseCredKeyAlgType);
        // noinspection ConstantConditions
        certRecordConfig.setKeyTag(ToolDnsRecordUtils.getKeyTag(discoveryTestcaseCredKeyAlgType, discoveryTestcaseCredKeyInfo.getPublicKey()));

        // noinspection ConstantConditions
        certRecordConfig.setCertificateData(CertificateUtils.writeCertificate(discoveryTestcaseCredCertInfo.getCertificate(), DataEncoding.DER));

        return certRecordConfig;
    }

    private boolean hasDiscoveryTestcaseCredentialCertRecord(DiscoveryTestcaseCredential discoveryTestcaseCred) throws Exception {
        DiscoveryTestcaseCredentialLocation discoveryTestcaseCredLoc;
        MailAddress discoveryTestcaseCredLocMailAddr;
        CredentialInfo discoveryTestcaseCredInfo;
        KeyInfo discoveryTestcaseCredKeyInfo;

        // noinspection ConstantConditions
        return discoveryTestcaseCred.hasLocation() && (discoveryTestcaseCredLoc = discoveryTestcaseCred.getLocation()).getType().isDns()
            && discoveryTestcaseCredLoc.hasMailAddress()
            && (discoveryTestcaseCredLocMailAddr = discoveryTestcaseCredLoc.getMailAddress()).getBindingType().isBound()
            && ToolDnsNameUtils.toAbsolute(discoveryTestcaseCredLocMailAddr.getDomainName()).equals(InstanceDnsConfigImpl.this.domainName)
            && discoveryTestcaseCred.hasCredentialInfo() && (discoveryTestcaseCredInfo = discoveryTestcaseCred.getCredentialInfo()).hasKeyDescriptor()
            && (discoveryTestcaseCredKeyInfo = discoveryTestcaseCredInfo.getKeyDescriptor()).hasKeyAlgorithm()
            && discoveryTestcaseCredKeyInfo.hasPublicKey() && discoveryTestcaseCredInfo.hasCertificateDescriptor()
            && discoveryTestcaseCredInfo.getCertificateDescriptor().hasCertificate();
    }

    private AbstractApplicationContext appContext;
    private List<ARecordConfig> aRecordsConfigs;
    private List<CertRecordConfig> certRecordConfigs;
    private List<CnameRecordConfig> cnameRecordConfigs;
    private List<MxRecordConfig> mxRecordConfigs;
    private List<NsRecordConfig> nsRecordConfigs;
    private List<PtrRecordConfig> ptrRecordConfigs;
    private SoaRecordConfig soaRecordConfig;
    private List<SrvRecordConfig> srvRecordConfigs;
    private List<TxtRecordConfig> txtRecordConfigs;
    private Map<Name, Map<DnsRecordType, List<Record>>> nameRecordsMap = new TreeMap<>();

    @Nullable
    @Override
    public List<Record> findAnswers(Record questionRecord) {
        // noinspection ConstantConditions
        return this.findAnswers(ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType()), questionRecord.getName());
    }

    @Nullable
    @Override
    public List<Record> findAnswers(DnsRecordType questionRecordType, Name questionName) {
        Map<DnsRecordType, List<Record>> recordsMap = this.nameRecordsMap.get(questionName);

        return ((recordsMap != null) ? recordsMap.get(((questionRecordType != DnsRecordType.AAAA) ? questionRecordType : DnsRecordType.A)) : null);
    }

    @Override
    public boolean isAuthoritative(Record questionRecord) {
        // noinspection ConstantConditions
        return this.isAuthoritative(ToolDnsUtils.findByCode(DnsRecordType.class, questionRecord.getType()), questionRecord.getName());
    }

    @Override
    public boolean isAuthoritative(DnsRecordType questionRecordType, Name questionName) {
        return (this.isAuthoritative() && ((questionRecordType != DnsRecordType.PTR) ? questionName.subdomain(this.domainName) : questionName.equals(ReverseMap
            .fromAddress(this.ipAddr))));
    }

    @Override
    public boolean isAuthoritative() {
        return (this.hasDomainName() && this.hasIpAddress() && this.hasSoaRecordConfig());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!this.hasDomainName() || !this.hasIpAddress()) {
            return;
        }

        this.domainName = ToolDnsNameUtils.toAbsolute(this.domainName);

        List<? extends DnsRecordConfig<? extends Record>> recordConfigs = null;
        ARecordConfig aRecordConfig;
        TargetedDnsRecordConfig<? extends Record> targetedRecordConfig;
        SoaRecordConfig soaRecordConfig;
        Name recordName;
        Record record;
        Map<DnsRecordType, List<Record>> recordsMap;

        for (DnsRecordType recordType : EnumSet.allOf(DnsRecordType.class)) {
            if (!recordType.isProcessed()) {
                continue;
            }

            switch (recordType) {
                case A:
                    recordConfigs = this.aRecordsConfigs;
                    break;

                case CERT:
                    recordConfigs =
                        this.certRecordConfigs =
                            (List<CertRecordConfig>) ToolCollectionUtils.nullIfEmpty(ToolStreamUtils.transform(ToolStreamUtils.filter(IteratorUtils
                                .asIterable(ToolIteratorUtils
                                    .chainedIterator(ToolStreamUtils.transform(ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class),
                                        DiscoveryTestcase::extractCredentials))), ToolPredicate.wrap(
                                this::hasDiscoveryTestcaseCredentialCertRecord)), ToolTransformer.wrap(
                                this::transformDiscoveryTestcaseCredentialCertRecordConfig)));
                    break;

                case CNAME:
                    recordConfigs = this.cnameRecordConfigs;
                    break;

                case MX:
                    recordConfigs = this.mxRecordConfigs;
                    break;

                case NS:
                    recordConfigs = this.nsRecordConfigs;
                    break;

                case PTR:
                    recordConfigs =
                        this.ptrRecordConfigs =
                            (List<PtrRecordConfig>) ToolCollectionUtils.nullIfEmpty(ToolStreamUtils.transform(this.aRecordsConfigs,
                                this::transformReverseMapPtrRecordConfig));
                    break;

                case SOA:
                    recordConfigs = ToolArrayUtils.asList(this.soaRecordConfig);
                    break;

                case SRV:
                    recordConfigs = this.srvRecordConfigs;
                    break;

                case TXT:
                    recordConfigs = this.txtRecordConfigs;
                    break;
            }

            if (CollectionUtils.isEmpty(recordConfigs)) {
                continue;
            }

            // noinspection ConstantConditions
            for (DnsRecordConfig<? extends Record> recordConfig : recordConfigs) {
                switch (recordType) {
                    case A:
                        if ((aRecordConfig = ((ARecordConfig) recordConfig)).getAddress() == null) {
                            aRecordConfig.setAddress(this.ipAddr);
                        }
                        break;

                    case CNAME:
                    case MX:
                    case NS:
                    case SRV:
                        if ((targetedRecordConfig = (TargetedDnsRecordConfig<? extends Record>) recordConfig).getTarget() == null) {
                            targetedRecordConfig.setTarget(this.domainName);
                        }

                        targetedRecordConfig.setTarget(ToolDnsNameUtils.toAbsolute(targetedRecordConfig.getTarget()));
                        break;

                    case SOA:
                        (soaRecordConfig = ((SoaRecordConfig) recordConfig)).setAdmin(ToolDnsNameUtils.toAbsolute(soaRecordConfig.getAdmin()));
                        soaRecordConfig.setHost(ToolDnsNameUtils.toAbsolute(soaRecordConfig.getHost()));
                        break;
                }

                if (((recordName = recordConfig.getName()) == null) || !recordName.isAbsolute()) {
                    recordConfig.setName(ToolDnsNameUtils.toAbsolute(ToolDnsNameUtils.fromLabels(recordName, this.domainName)));
                }

                this.nameRecordsMap.putIfAbsent((recordName = (record = recordConfig.toRecord()).getName()), new EnumMap<>(DnsRecordType.class));

                (recordsMap = this.nameRecordsMap.get(recordName)).putIfAbsent(recordType, new ArrayList<>());
                recordsMap.get(recordType).add(record);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public boolean hasARecordConfigs() {
        return !CollectionUtils.isEmpty(this.aRecordsConfigs);
    }

    @Nullable
    @Override
    public List<ARecordConfig> getARecordConfigs() {
        return this.aRecordsConfigs;
    }

    @Override
    public void setARecordConfigs(@Nullable List<ARecordConfig> aRecordConfigs) {
        this.aRecordsConfigs = aRecordConfigs;
    }

    @Override
    public boolean hasCertRecordConfigs() {
        return !CollectionUtils.isEmpty(this.certRecordConfigs);
    }

    @Nullable
    @Override
    public List<CertRecordConfig> getCertRecordConfigs() {
        return this.certRecordConfigs;
    }

    @Override
    public boolean hasCnameRecordConfigs() {
        return !CollectionUtils.isEmpty(this.cnameRecordConfigs);
    }

    @Nullable
    @Override
    public List<CnameRecordConfig> getCnameRecordConfigs() {
        return this.cnameRecordConfigs;
    }

    @Override
    public void setCnameRecordConfigs(@Nullable List<CnameRecordConfig> cnameRecordConfigs) {
        this.cnameRecordConfigs = cnameRecordConfigs;
    }

    @Override
    public boolean hasMxRecordConfigs() {
        return !CollectionUtils.isEmpty(this.mxRecordConfigs);
    }

    @Nullable
    @Override
    public List<MxRecordConfig> getMxRecordConfigs() {
        return this.mxRecordConfigs;
    }

    @Override
    public void setMxRecordConfigs(@Nullable List<MxRecordConfig> mxRecordConfigs) {
        this.mxRecordConfigs = mxRecordConfigs;
    }

    @Override
    public Map<Name, Map<DnsRecordType, List<Record>>> getNameRecordsMap() {
        return this.nameRecordsMap;
    }

    @Override
    public boolean hasNsRecordConfigs() {
        return !CollectionUtils.isEmpty(this.nsRecordConfigs);
    }

    @Nullable
    @Override
    public List<NsRecordConfig> getNsRecordConfigs() {
        return this.nsRecordConfigs;
    }

    @Override
    public void setNsRecordConfigs(@Nullable List<NsRecordConfig> nsRecordConfigs) {
        this.nsRecordConfigs = nsRecordConfigs;
    }

    @Override
    public boolean hasPtrRecordConfigs() {
        return !CollectionUtils.isEmpty(this.ptrRecordConfigs);
    }

    @Nullable
    @Override
    public List<PtrRecordConfig> getPtrRecordConfigs() {
        return this.ptrRecordConfigs;
    }

    @Override
    public boolean hasSoaRecordConfig() {
        return this.soaRecordConfig != null;
    }

    @Nullable
    @Override
    public SoaRecordConfig getSoaRecordConfig() {
        return this.soaRecordConfig;
    }

    @Override
    public void setSoaRecordConfig(@Nullable SoaRecordConfig soaRecordConfig) {
        this.soaRecordConfig = soaRecordConfig;
    }

    @Override
    public boolean hasSrvRecordConfigs() {
        return !CollectionUtils.isEmpty(this.srvRecordConfigs);
    }

    @Nullable
    @Override
    public List<SrvRecordConfig> getSrvRecordConfigs() {
        return this.srvRecordConfigs;
    }

    @Override
    public void setSrvRecordConfigs(@Nullable List<SrvRecordConfig> srvRecordConfigs) {
        this.srvRecordConfigs = srvRecordConfigs;
    }

    @Override
    public boolean hasTxtRecordConfigs() {
        return !CollectionUtils.isEmpty(this.txtRecordConfigs);
    }

    @Nullable
    @Override
    public List<TxtRecordConfig> getTxtRecordConfigs() {
        return this.txtRecordConfigs;
    }

    @Override
    public void setTxtRecordConfigs(@Nullable List<TxtRecordConfig> txtRecordConfigs) {
        this.txtRecordConfigs = txtRecordConfigs;
    }
}
