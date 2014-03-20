package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailRepository;
import gov.hhs.onc.dcdt.service.mail.james.ToolMailRepositoryStore;
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
    private Map<String, String> repoProtocolBeanNameMap = new HashMap<>();
    private Map<String, ToolMailRepository> repoMap = new HashMap<>();

    @Override
    public MailRepository select(String repoUrlStr) throws MailRepositoryStoreException {
        if (this.repoMap.containsKey(repoUrlStr)) {
            return this.repoMap.get(repoUrlStr);
        }

        String[] repoUrlStrParts = StringUtils.split(repoUrlStr, ToolResourceUtils.DELIM_URL_PREFIX, 2);

        if (repoUrlStrParts.length != 2) {
            throw new MailRepositoryStoreException(String.format("James mail repository URL string does not contain a prefix: %s", repoUrlStr));
        } else if (!this.repoProtocolBeanNameMap.containsKey(repoUrlStrParts[0])) {
            throw new MailRepositoryStoreException(String.format("Unknown James mail repository URL string prefix: %s", repoUrlStrParts[0]));
        }

        ToolMailRepository repo =
            ToolBeanFactoryUtils.createBean(this.appContext, this.repoProtocolBeanNameMap.get(repoUrlStrParts[0]), ToolMailRepository.class);
        repo.setName(repoUrlStrParts[1]);

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
    public Map<String, String> getRepositoryProtocolBeanNameMap() {
        return this.repoProtocolBeanNameMap;
    }

    @Override
    public void setRepositoryProtocolBeanNameMap(Map<String, String> repoProtocolBeanNameMap) {
        this.repoProtocolBeanNameMap.clear();
        this.repoProtocolBeanNameMap.putAll(repoProtocolBeanNameMap);
    }
}
