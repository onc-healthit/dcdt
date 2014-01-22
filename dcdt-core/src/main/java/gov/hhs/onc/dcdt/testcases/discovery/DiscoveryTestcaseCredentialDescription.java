package gov.hhs.onc.dcdt.testcases.discovery;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseCredentialDescriptionImpl;

@JsonSubTypes({ @Type(DiscoveryTestcaseCredentialDescriptionImpl.class) })
public interface DiscoveryTestcaseCredentialDescription extends ToolDescriptionBean {
}
