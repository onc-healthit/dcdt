package gov.hhs.onc.dcdt.mail;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.mail.internet.InternetAddress;
import org.xbill.DNS.Name;

@JsonTypeInfo(use = Id.NONE)
public interface MailAddress extends Serializable, ToolBean {
    @Nullable
    public MailAddress forBindingType(BindingType bindingType);

    public BindingType getBindingType();

    @Nullable
    public InternetAddress toInternetAddress() throws ToolMailAddressException;

    @Nullable
    public InternetAddress toInternetAddress(boolean includePersonalPart) throws ToolMailAddressException;

    public boolean hasAddressName();

    @Nullable
    public Name getAddressName() throws ToolMailAddressException;

    @Nullable
    public Name toAddressName() throws ToolMailAddressException;

    @Nullable
    public String toAddress();

    @Nullable
    public String toAddress(BindingType bindingType);

    @Nullable
    public Name toAddressName(BindingType bindingType) throws ToolMailAddressException;

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

    public boolean hasPersonalPart();

    @Nullable
    public String getPersonalPart();

    public void setPersonalPart(@Nullable String personalPart);

    public boolean hasPart(MailAddressPart part);

    @Nullable
    public String getPart(MailAddressPart part);

    public void setPart(MailAddressPart part, @Nullable String partValue);
}
