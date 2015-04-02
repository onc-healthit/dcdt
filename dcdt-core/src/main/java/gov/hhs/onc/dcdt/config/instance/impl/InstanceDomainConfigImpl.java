package gov.hhs.onc.dcdt.config.instance.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDomainBean;
import gov.hhs.onc.dcdt.config.instance.InstanceDomainConfig;

@JsonTypeName("instanceDomainConfig")
public class InstanceDomainConfigImpl extends AbstractToolDomainBean implements InstanceDomainConfig {
}
