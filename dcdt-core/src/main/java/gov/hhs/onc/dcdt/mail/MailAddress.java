package gov.hhs.onc.dcdt.mail;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;
import javax.mail.internet.InternetAddress;
import org.xbill.DNS.Name;

@JsonTypeInfo(use = Id.NONE)
public interface MailAddress extends ToolBean {
    @Nullable
    public MailAddress forBindingType(BindingType bindingType);

    public BindingType getBindingType();

    @Nullable
    public InternetAddress toInternetAddress() throws ToolMailAddressException;

    @Nullable
    public Name toAddressName() throws ToolMailAddressException;

    @Nullable
    public String toAddress();

    public String[] toAddressParts();

    public boolean hasDomainName();

    @Nullable
    public Name getDomainName() throws ToolMailAddressException;

    public void setDomainName(@Nullable Name domainName);

    public boolean hasDomainNamePart();

    @Nullable
    public String getDomainNamePart();

    public void setDomainNamePart(@Nullable String domainNamePart);

    public boolean hasLocalPart();

    @Nullable
    public String getLocalPart();

    public void setLocalPart(@Nullable String localPart);
}
