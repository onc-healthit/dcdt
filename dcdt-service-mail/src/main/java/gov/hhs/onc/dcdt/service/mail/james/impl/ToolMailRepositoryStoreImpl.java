package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.service.mail.james.MailRepositoryProtocol;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailRepository;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailRepositoryStore;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mailrepository.api.MailRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ToolMailRepositoryStoreImpl extends AbstractToolBean implements ToolMailRepositoryStore {
    private AbstractApplicationContext appContext;
    private MailRepositoryProtocol defaultRepoProtocol;
    private Map<MailRepositoryProtocol, String> repoProtocolBeanNames = new HashMap<>();
    private Map<String, ToolMailRepository> repoMap = new HashMap<>();

    @Override
    public MailRepository select(String repoUrlStr) throws MailRepositoryStoreException {
        if (this.repoMap.containsKey(repoUrlStr)) {
            return this.repoMap.get(repoUrlStr);
        }

        String[] repoUrlStrParts = StringUtils.split(repoUrlStr, ToolResourceUtils.DELIM_URL_PREFIX, 2);
        MailRepositoryProtocol repoProtocol;

        if (repoUrlStrParts.length == 2) {
            try {
                repoProtocol = MailRepositoryProtocol.valueOf(StringUtils.upperCase(repoUrlStrParts[0]));
            } catch (IllegalArgumentException e) {
                throw new MailRepositoryStoreException(String.format("Unknown James mail repository protocol: %s", repoUrlStrParts[0]));
            }
        } else {
            repoProtocol = this.defaultRepoProtocol;
        }

        if (!this.repoProtocolBeanNames.containsKey(repoProtocol)) {
            throw new MailRepositoryStoreException(String.format("Unknown James mail repository protocol: %s", repoProtocol));
        }

        ToolMailRepository repo = ToolBeanFactoryUtils.createBean(this.appContext, this.repoProtocolBeanNames.get(repoProtocol), ToolMailRepository.class);
        repo.setName(ToolArrayUtils.getLast(repoUrlStrParts));

        this.repoMap.put(repoUrlStr, repo);

        return repo;
    }

    @Override
    public List<String> getUrls() {
        return new ArrayList<>(this.repoMap.keySet());
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public MailRepositoryProtocol getDefaultRepositoryProtocol() {
        return this.defaultRepoProtocol;
    }

    @Override
    public void setDefaultRepositoryProtocol(MailRepositoryProtocol defaultRepoProtocol) {
        this.defaultRepoProtocol = defaultRepoProtocol;
    }

    @Override
    public Map<MailRepositoryProtocol, String> getRepositoryProtocolBeanNames() {
        return this.repoProtocolBeanNames;
    }

    @Override
    public void setRepositoryProtocolBeanNames(Map<MailRepositoryProtocol, String> repoProtocolBeanNames) {
        this.repoProtocolBeanNames.clear();
        this.repoProtocolBeanNames.putAll(repoProtocolBeanNames);
    }
}
