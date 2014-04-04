package gov.hhs.onc.dcdt.dns.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.HasMxRecord;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordOrderUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolConstraintValidator;
import java.util.List;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.MXRecord;

public class HasMxRecordConstraintValidator extends AbstractToolConstraintValidator<HasMxRecord, String> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    private final static String MXRECORD_TARGET_ERROR_CODE = "{dcdt.dns.validation.constraints.ResolveMxRecordTarget.msg}";
    private final static String MXRECORD_LOOKUP_FAILURE_ERROR_CODE = "{dcdt.dns.validation.constraints.MxRecordLookupFailure.msg}";

    @Override
    public boolean isValid(String mailAddrStr, ConstraintValidatorContext validatorContext) {
        MailAddress mailAddr = new MailAddressImpl(mailAddrStr);

        try {
            List<MXRecord> mxRecords = this.dnsLookupService.lookupMxRecords(mailAddr.toAddressName(BindingType.DOMAIN)).getResolvedAnswers();

            if (!CollectionUtils.isEmpty(mxRecords)) {
                if (resolveMxRecordTarget(mxRecords)) {
                    return true;
                }

                validatorContext.disableDefaultConstraintViolation();
                validatorContext.buildConstraintViolationWithTemplate(MXRECORD_TARGET_ERROR_CODE).addConstraintViolation();
            }
        } catch (DnsException | ToolMailAddressException e) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext.buildConstraintViolationWithTemplate(MXRECORD_LOOKUP_FAILURE_ERROR_CODE).addConstraintViolation();
        }

        return false;
    }

    private boolean resolveMxRecordTarget(List<MXRecord> mxRecords) throws DnsException {
        for (MXRecord record : IteratorUtils.asIterable(ToolDnsRecordOrderUtils.buildMxRecordIterator(mxRecords))) {
            if (!CollectionUtils.isEmpty(this.dnsLookupService.lookupARecords(record.getTarget()).getResolvedAnswers())) {
                return true;
            }
        }

        return false;
    }
}
