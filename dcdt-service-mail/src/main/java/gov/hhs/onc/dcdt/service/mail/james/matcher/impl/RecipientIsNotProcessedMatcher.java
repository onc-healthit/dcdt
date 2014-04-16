package gov.hhs.onc.dcdt.service.mail.james.matcher.impl;

import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.service.mail.james.ToolUsersRepository;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import javax.annotation.Resource;
import org.apache.mailet.MailAddress;

public class RecipientIsNotProcessedMatcher extends AbstractToolMatcher {
    @Resource
    private ToolUsersRepository userRepo;

    @Override
    protected Collection<MailAddress> matchInternal(ToolMimeMessageHelper mimeMsgHelper) throws Exception {
        gov.hhs.onc.dcdt.mail.MailAddress to = mimeMsgHelper.getTo();

        // noinspection ConstantConditions
        return (!this.userRepo.isProcessed(to.toAddress()) ? ToolArrayUtils.asList(new MailAddress(to.toAddress())) : super.matchInternal(mimeMsgHelper));
    }
}
