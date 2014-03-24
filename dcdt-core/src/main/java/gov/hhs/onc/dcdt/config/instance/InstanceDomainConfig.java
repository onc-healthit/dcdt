package gov.hhs.onc.dcdt.config.instance;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDomainBean;
import gov.hhs.onc.dcdt.config.instance.impl.InstanceDomainConfigImpl;

@JsonSubTypes({ @Type(InstanceDomainConfigImpl.class) })
public interface InstanceDomainConfig extends ToolDomainBean {
}
