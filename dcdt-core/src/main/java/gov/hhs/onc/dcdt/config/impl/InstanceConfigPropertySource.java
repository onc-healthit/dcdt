package gov.hhs.onc.dcdt.config.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPropertySource;
import gov.hhs.onc.dcdt.config.InstanceConfig;
import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public class InstanceConfigPropertySource extends AbstractToolBeanPropertySource<InstanceConfig> {
    private final static String PROP_NAME_PREFIX_INSTANCE = ToolPropertyUtils.joinName(PROP_NAME_PREFIX, "instance");

    public InstanceConfigPropertySource() {
        super(InstanceConfig.class, PROP_NAME_PREFIX_INSTANCE);
    }
}
