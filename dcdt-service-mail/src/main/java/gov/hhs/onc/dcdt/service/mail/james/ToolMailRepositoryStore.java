package gov.hhs.onc.dcdt.service.mail.james;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.Map;
import org.apache.james.mailrepository.api.MailRepositoryStore;
import org.springframework.context.ApplicationContextAware;

public interface ToolMailRepositoryStore extends ApplicationContextAware, MailRepositoryStore, ToolBean {
    public MailRepositoryProtocol getDefaultRepositoryProtocol();

    public void setDefaultRepositoryProtocol(MailRepositoryProtocol defaultRepoProtocol);

    public Map<MailRepositoryProtocol, String> getRepositoryProtocolBeanNames();

    public void setRepositoryProtocolBeanNames(Map<MailRepositoryProtocol, String> repoProtocolBeanNames);
}
