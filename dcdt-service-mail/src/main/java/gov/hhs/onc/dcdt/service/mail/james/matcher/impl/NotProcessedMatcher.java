package gov.hhs.onc.dcdt.service.mail.james.matcher.impl;

import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.james.ToolUsersRepository;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import java.util.Objects;
import javax.annotation.Resource;
import javax.mail.MessagingException;

public class NotProcessedMatcher extends AbstractToolMatcher {
    @Resource
    private ToolUsersRepository userRepo;

    @Override
    protected Collection<MailAddress> matchInternal(ToolMimeMessageHelper msgHelper) throws MessagingException {
        MailAddress to = msgHelper.getTo();

        // noinspection ConstantConditions
        return ((!this.userRepo.isProcessed(Objects.toString(msgHelper.getFrom(), null)) && !this.userRepo.isProcessed(Objects.toString(to, null)))
            ? ToolArrayUtils.asList(to) : MATCH_ADDRS_NONE);
    }
}
