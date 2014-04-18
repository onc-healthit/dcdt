package gov.hhs.onc.dcdt.discovery.steps;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import gov.hhs.onc.dcdt.discovery.steps.impl.CertificateDiscoveryStepDescriptionImpl;

@JsonSubTypes({ @Type(CertificateDiscoveryStepDescriptionImpl.class) })
public interface CertificateDiscoveryStepDescription extends ToolDescriptionBean {
}
