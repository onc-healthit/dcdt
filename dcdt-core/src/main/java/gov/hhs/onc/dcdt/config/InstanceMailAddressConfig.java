package gov.hhs.onc.dcdt.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import gov.hhs.onc.dcdt.config.impl.InstanceMailAddressConfigImpl;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.config.MailGatewayConfig;
import gov.hhs.onc.dcdt.mail.config.MailGatewayCredentialConfig;

@JsonSubTypes({ @Type(InstanceMailAddressConfigImpl.class) })
public interface InstanceMailAddressConfig extends ToolNamedBean {
    @JsonProperty("gatewayConfig")
    public MailGatewayConfig getGatewayConfig();

    public void setGatewayConfig(MailGatewayConfig gatewayConfig);

    @JsonProperty("gatewayCredConfig")
    public MailGatewayCredentialConfig getGatewayCredentialConfig();

    public void setGatewayCredentialConfig(MailGatewayCredentialConfig gatewayCredConfig);

    @JsonProperty("mailAddr")
    public MailAddress getMailAddress();

    public void setMailAddress(MailAddress mailAddr);
}
