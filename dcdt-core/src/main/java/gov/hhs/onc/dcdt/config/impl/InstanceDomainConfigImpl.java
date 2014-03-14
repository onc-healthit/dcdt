package gov.hhs.onc.dcdt.config.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDomainBean;
import gov.hhs.onc.dcdt.config.InstanceDomainConfig;

@JsonTypeName("instanceDomainConfig")
public class InstanceDomainConfigImpl extends AbstractToolDomainBean implements InstanceDomainConfig {
}
