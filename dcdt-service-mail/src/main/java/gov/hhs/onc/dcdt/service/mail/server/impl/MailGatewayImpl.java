package gov.hhs.onc.dcdt.service.mail.server.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.service.mail.server.MailGateway;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("mailGatewayImpl")
public class MailGatewayImpl extends AbstractToolBean implements MailGateway {
    private final static Logger LOGGER = LoggerFactory.getLogger(MailGatewayImpl.class);

    private AbstractApplicationContext appContext;
    private Map<MailAddress, InstanceMailAddressConfig> addrConfigs;
    private Map<MailAddress, DiscoveryTestcase> discoveryTestcaseAddrs;
    private Map<MailAddress, String> authCreds;

    @Nullable
    @Override
    public InstanceMailAddressConfig authenticate(String id, String secret) {
        if (!this.hasAddressConfigs() || MapUtils.isEmpty(this.authCreds) || !ToolMailAddressUtils.isAddress(id)) {
            return null;
        }

        MailAddress addr = new MailAddressImpl(id);

        if (!this.addrConfigs.containsKey(addr) || !this.authCreds.containsKey(addr)) {
            return null;
        }

        // noinspection ConstantConditions
        return (this.authCreds.get(addr).equals(secret) ? this.addrConfigs.get(addr) : null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // noinspection ConstantConditions
        if (!ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class).isConfigured()) {
            return;
        }

        this.authCreds =
            new LinkedHashMap<>((this.addrConfigs =
                ToolBeanFactoryUtils.getBeansOfType(this.appContext, InstanceMailAddressConfig.class).stream()
                    .collect(ToolStreamUtils.toMap(InstanceMailAddressConfig::getMailAddress, Function.identity(), LinkedHashMap::new))).size());

        this.addrConfigs.forEach((addr, addrConfig) -> {
            String secret = addrConfig.getGatewayCredentialConfig().getSecret();

            this.authCreds.put(addr, secret);

            LOGGER.debug(String.format("Added mail gateway authentication credentials (addr=%s, secret=%s).", addr, secret));
        });

        this.discoveryTestcaseAddrs =
            ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class).stream()
                .collect(ToolStreamUtils.toMap(DiscoveryTestcase::getMailAddress, Function.identity(), LinkedHashMap::new));
    }

    @Override
    public boolean hasAddressConfigs() {
        return !MapUtils.isEmpty(this.addrConfigs);
    }

    @Nullable
    @Override
    public Map<MailAddress, InstanceMailAddressConfig> getAddressConfigs() {
        return this.addrConfigs;
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = ((AbstractApplicationContext) appContext);
    }

    @Override
    public boolean hasDiscoveryTestcaseAddresses() {
        return !MapUtils.isEmpty(this.discoveryTestcaseAddrs);
    }

    @Nullable
    @Override
    public Map<MailAddress, DiscoveryTestcase> getDiscoveryTestcaseAddresses() {
        return this.discoveryTestcaseAddrs;
    }
}
