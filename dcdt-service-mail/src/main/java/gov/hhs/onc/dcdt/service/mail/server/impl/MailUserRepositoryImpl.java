package gov.hhs.onc.dcdt.service.mail.server.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.service.mail.server.MailUserRepository;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("mailUserRepoImpl")
public class MailUserRepositoryImpl extends AbstractToolBean implements MailUserRepository {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailUserRepositoryImpl.class);

    private AbstractApplicationContext appContext;
    private MultiKeyMap<String, InstanceMailAddressConfig> authConfigs;

    @Nullable
    @Override
    public InstanceMailAddressConfig findAuthenticatedConfig(String id, String secret) {
        return this.authConfigs.get(id, secret);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // noinspection ConstantConditions
        if (ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class).isConfigured()) {
            List<InstanceMailAddressConfig> configs = ToolBeanFactoryUtils.getBeansOfType(this.appContext, InstanceMailAddressConfig.class);
            String userMailAddr, userSecret;

            this.authConfigs = MultiKeyMap.multiKeyMap(new LinkedMap<>(configs.size()));

            for (InstanceMailAddressConfig config : configs) {
                this.authConfigs.put((userMailAddr = config.getMailAddress().toAddress(BindingType.ADDRESS)), (userSecret =
                    config.getGatewayCredentialConfig().getSecret()), config);

                LOGGER.debug(String.format("Added mail service user (mailAddr=%s, secret=%s).", userMailAddr, userSecret));
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public boolean hasAuthenticatedConfigs() {
        return !MapUtils.isEmpty(this.authConfigs);
    }

    @Nullable
    @Override
    public MultiKeyMap<String, InstanceMailAddressConfig> getAuthenticatedConfigs() {
        return this.authConfigs;
    }
}
