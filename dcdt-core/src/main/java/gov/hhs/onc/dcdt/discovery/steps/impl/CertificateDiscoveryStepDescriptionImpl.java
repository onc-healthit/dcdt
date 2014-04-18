package gov.hhs.onc.dcdt.discovery.steps.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolDescriptionBean;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStepDescription;

@JsonTypeName("certDiscoveryStepDesc")
public class CertificateDiscoveryStepDescriptionImpl extends AbstractToolDescriptionBean implements CertificateDiscoveryStepDescription {
}
