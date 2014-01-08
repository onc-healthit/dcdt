package gov.hhs.onc.dcdt.config;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.data.tx.services.ToolBeanService;

public interface InstanceConfigService extends ToolBeanService<InstanceConfig, InstanceConfigDao> {
    public InstanceConfig processInstanceConfig() throws CryptographyException;
}
