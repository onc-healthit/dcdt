package gov.hhs.onc.dcdt.discovery.steps.dns.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.LocationType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.dns.DnsLookupStep;
import gov.hhs.onc.dcdt.discovery.steps.impl.AbstractLookupStep;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.DnsRecordType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.List;
import javax.annotation.Nullable;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public abstract class AbstractDnsLookupStep<T extends Record> extends AbstractLookupStep<DnsLookupResult<T>, DnsLookupService> implements DnsLookupStep<T> {
    protected Class<T> recordClass;
    protected DnsRecordType recordType;

    protected AbstractDnsLookupStep(BindingType bindingType, DnsLookupService lookupService, DnsRecordType recordType, Class<T> recordClass) {
        super(bindingType, LocationType.DNS, lookupService);

        this.recordType = recordType;
        this.recordClass = recordClass;
    }

    @Nullable
    @Override
    protected DnsLookupResult<T> executeLookup(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        DnsLookupResult<T> lookupResult = null;

        try {
            Name directAddrName = this.buildDirectAddressName(directAddr);

            if ((lookupResult = this.lookupService.lookupRecords(this.recordType, this.recordClass, directAddrName)).isSuccess()) {
                this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.INFO, String.format(
                    "DNS lookup (recordType=%s, directAddrName=%s) was successful: [%s]", this.recordType.getId(), directAddrName,
                    ToolStringUtils.joinDelimit(lookupResult.getAnswers(), ", "))));
            } else {
                this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, String.format("DNS lookup (recordType=%s, directAddrName=%s) failed (type=%s).",
                    this.recordType.getId(), directAddrName, lookupResult.getType().name())));
            }
        } catch (DnsException | ToolMailAddressException e) {
            this.execMsgs.add(new ToolMessageImpl(ToolMessageLevel.ERROR, String.format("DNS lookup (recordType=%s, directAddr=%s) failed: %s",
                this.recordType.getId(), directAddr.toAddress(), e.getMessage())));
            this.execSuccess = false;
        }

        return lookupResult;
    }

    protected Name buildDirectAddressName(MailAddress directAddr) throws DnsException, ToolMailAddressException {
        return directAddr.toAddressName();
    }

    @Override
    public Class<T> getRecordClass() {
        return this.recordClass;
    }

    @Override
    public DnsRecordType getRecordType() {
        return this.recordType;
    }
}
