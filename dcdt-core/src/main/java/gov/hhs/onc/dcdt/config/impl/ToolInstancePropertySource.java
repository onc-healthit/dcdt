package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.beans.factory.impl.AbstractToolBeanPropertySource;
import gov.hhs.onc.dcdt.config.ToolInstance;
import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;

public class ToolInstancePropertySource extends AbstractToolBeanPropertySource<ToolInstance> {
    private final static String PROP_NAME_PREFIX_INSTANCE = ToolPropertyUtils.joinName(PROP_NAME_PREFIX_TOOL, "instance");

    public ToolInstancePropertySource() {
        super(ToolInstance.class, PROP_NAME_PREFIX_INSTANCE);
    }
}
