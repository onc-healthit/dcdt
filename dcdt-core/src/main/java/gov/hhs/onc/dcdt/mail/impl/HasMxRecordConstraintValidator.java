package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsRecordOrderUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.HasMxRecord;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolStringConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.MXRecord;

public class HasMxRecordConstraintValidator extends AbstractToolStringConstraintValidator<HasMxRecord> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    @Override
    protected boolean isValidInternal(String value, ConstraintValidatorContext validatorContext) throws Exception {
        MailAddress mailAddr = new MailAddressImpl(value);

        try {
            DnsLookupResult<MXRecord> mxRecordLookupResult = this.dnsLookupService.lookupMxRecords(mailAddr.toAddressName(BindingType.DOMAIN));

            if (mxRecordLookupResult.getType().isSuccess() && mxRecordLookupResult.hasResolvedAnswers()) {
                DnsLookupResult<ARecord> targetARecordLookupResult;

                // noinspection ConstantConditions
                for (MXRecord mxRecord : IteratorUtils.asIterable(ToolDnsRecordOrderUtils.buildMxRecordIterator(mxRecordLookupResult.getResolvedAnswers()))) {
                    if ((targetARecordLookupResult = this.dnsLookupService.lookupARecords(mxRecord.getTarget())).getType().isSuccess()
                        && targetARecordLookupResult.hasResolvedAnswers()) {
                        return true;
                    }
                }

                validatorContext.disableDefaultConstraintViolation();
                validatorContext.buildConstraintViolationWithTemplate(this.anno.messageLookupTargets()).addConstraintViolation();
            }
        } catch (DnsException | ToolMailAddressException e) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext.buildConstraintViolationWithTemplate(this.anno.messageLookup()).addConstraintViolation();
        }

        return false;
    }
}
