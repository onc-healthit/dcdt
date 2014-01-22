package gov.hhs.onc.dcdt.testcases.discovery.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseCredentialDescription;

@JsonTypeName("discoveryTestcaseCredDesc")
public class DiscoveryTestcaseCredentialDescriptionImpl extends AbstractToolDescriptionBean implements DiscoveryTestcaseCredentialDescription {
}
