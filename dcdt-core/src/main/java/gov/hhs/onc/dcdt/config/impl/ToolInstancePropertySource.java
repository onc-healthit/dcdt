package gov.hhs.onc.dcdt.config.impl;


import gov.hhs.onc.dcdt.config.ToolInstance;

public class ToolInstancePropertySource extends AbstractToolBeanPropertySource<ToolInstance> {
    private final static String PROP_SOURCE_NAME = "toolInstancePropSource";

    private final static String PROP_NAME_PREFIX_INSTANCE = PROP_NAME_PREFIX + "instance.";

    public ToolInstancePropertySource(ToolInstance instance) {
        super(PROP_SOURCE_NAME, instance, PROP_NAME_PREFIX_INSTANCE);
    }
}
