package gov.hhs.onc.dcdt.testcases.discovery.credentials.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialDescription;

@JsonTypeName("discoveryTestcaseCredDesc")
public class DiscoveryTestcaseCredentialDescriptionImpl extends AbstractToolDescriptionBean implements DiscoveryTestcaseCredentialDescription {
}
