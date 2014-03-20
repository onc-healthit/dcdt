package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import org.apache.james.mailrepository.api.MailRepository;

public interface ToolMailRepository extends MailRepository, ToolNamedBean {
}
