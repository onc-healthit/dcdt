package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupService;
import gov.hhs.onc.dcdt.mail.HasMxRecord;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.validation.constraints.impl.AbstractToolStringConstraintValidator;
import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.MXRecord;

public class HasMxRecordConstraintValidator extends AbstractToolStringConstraintValidator<HasMxRecord> {
    @Resource(name = "dnsLookupServiceExternal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DnsLookupService dnsLookupService;

    @Override
    protected boolean isValidInternal(String value, ConstraintValidatorContext validatorContext) throws Exception {
        MailAddress mailAddr = new MailAddressImpl(value);

        try {
            DnsLookupResult<MXRecord> mxRecordLookupResult = this.dnsLookupService.lookupMxRecords(mailAddr.toAddressName(BindingType.DOMAIN));

            if (mxRecordLookupResult.getType().isSuccess() && mxRecordLookupResult.hasOrderedAnswers()) {
                DnsLookupResult<ARecord> targetARecordLookupResult;

                // noinspection ConstantConditions
                for (MXRecord mxRecord : mxRecordLookupResult.getOrderedAnswers()) {
                    if ((targetARecordLookupResult = this.dnsLookupService.lookupARecords(mxRecord.getTarget())).getType().isSuccess()
                        && targetARecordLookupResult.hasAnswers()) {
                        return true;
                    }
                }

                validatorContext.disableDefaultConstraintViolation();
                validatorContext.buildConstraintViolationWithTemplate(this.anno.messageLookupTargets()).addConstraintViolation();
            }
        } catch (ToolMailAddressException e) {
            validatorContext.disableDefaultConstraintViolation();
            validatorContext.buildConstraintViolationWithTemplate(this.anno.messageLookup()).addConstraintViolation();
        }

        return false;
    }
}
