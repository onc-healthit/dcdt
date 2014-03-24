package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailAddressPart;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.EnumMap;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Name;

public class MailAddressImpl extends AbstractToolBean implements MailAddress, Serializable {
    private final static long serialVersionUID = 1L;
    private EnumMap<MailAddressPart, String> addrPartMap = new EnumMap<>(MailAddressPart.class);

    public MailAddressImpl() {
        this(null, null);
    }

    public MailAddressImpl(@Nullable Address addr) {
        this(Objects.toString(addr, null));
    }

    public MailAddressImpl(@Nullable String addr) {
        this(ToolMailAddressUtils.splitParts(addr));
    }

    public MailAddressImpl(@Nullable String[] addrParts) {
        if (addrParts == null) {
            return;
        }

        if (addrParts.length == 3) {
            this.setPersonalPart(addrParts[0]);
        }

        if (addrParts.length >= 2) {
            this.setLocalPart(addrParts[addrParts.length - 2]);
        }

        this.setDomainNamePart(addrParts[addrParts.length - 1]);
    }

    public MailAddressImpl(@Nullable String localPart, @Nullable String domainNamePart) {
        this(null, localPart, domainNamePart);
    }

    public MailAddressImpl(@Nullable String personalPart, @Nullable String localPart, @Nullable String domainNamePart) {
        this.setPersonalPart(personalPart);
        this.setLocalPart(localPart);
        this.setDomainNamePart(domainNamePart);
    }

    @Nullable
    @Override
    public MailAddress forBindingType(BindingType bindingType) {
        BindingType bindingTypeSelf = this.getBindingType();

        return ((bindingTypeSelf.isBound() && bindingType.isBound()) ? ((bindingType == bindingTypeSelf) ? new MailAddressImpl(this.getLocalPart(),
            this.getDomainNamePart()) : (bindingType.isDomainBound() ? new MailAddressImpl(null, this.getDomainNamePart()) : null)) : null);
    }

    @Override
    public BindingType getBindingType() {
        return (this.hasDomainNamePart() ? (this.hasLocalPart() ? BindingType.ADDRESS : BindingType.DOMAIN) : BindingType.NONE);
    }

    @Nullable
    @Override
    public InternetAddress toInternetAddress() throws ToolMailAddressException {
        return this.toInternetAddress(true);
    }

    @Nullable
    @Override
    public InternetAddress toInternetAddress(boolean includePersonalPart) throws ToolMailAddressException {
        String addr = this.toAddress();

        if (StringUtils.isBlank(addr)) {
            return null;
        }

        try {
            InternetAddress internetAddr = new InternetAddress(addr, true);

            if (includePersonalPart && this.hasPersonalPart()) {
                internetAddr.setPersonal(this.getPersonalPart());
            }

            return internetAddr;
        } catch (AddressException | UnsupportedEncodingException e) {
            throw new ToolMailAddressException(String.format("Unable to get Internet mail address from mail address string: %s", addr), e);
        }
    }

    @Override
    public boolean hasAddressName() {
        try {
            return this.getAddressName() != null;
        } catch (ToolMailAddressException ignored) {
        }

        return false;
    }

    @Nullable
    @Override
    public Name getAddressName() throws ToolMailAddressException {
        if (this.toAddress() != null) {
            try {
                // noinspection ConstantConditions
                return ToolDnsNameUtils.fromString(this.toAddress().replace(ToolMailAddressUtils.MAIL_ADDR_PART_DELIM, ToolDnsNameUtils.DNS_NAME_DELIM));
            } catch (DnsNameException e) {
                throw new ToolMailAddressException(String.format("Unable to get mail address from string: %s", this.toAddress()), e);
            }
        }
        return Name.empty;
    }

    @Nullable
    @Override
    public Name toAddressName() throws ToolMailAddressException {
        if (!this.getBindingType().isBound()) {
            return null;
        }

        String[] addrParts = this.toAddressParts();

        try {
            return ToolDnsNameUtils.fromLabelStrings(addrParts);
        } catch (DnsNameException e) {
            throw new ToolMailAddressException(String.format("Unable to get mail address DNS name from strings: [%s]",
                ToolStringUtils.joinDelimit(addrParts, ", ")), e);
        }
    }

    @Nullable
    @Override
    public Name toAddressName(BindingType bindingType) throws ToolMailAddressException {
        Name addrName = null;

        switch (bindingType) {
            case ADDRESS:
                addrName = this.getAddressName();
                break;
            case DOMAIN:
                addrName = this.getDomainName();
                break;
            default:
                break;
        }

        return addrName;
    }

    @Nullable
    @Override
    public String toAddress() {
        return ToolMailAddressUtils.joinParts(this.toAddressParts());
    }

    @Nullable
    @Override
    public String toAddress(BindingType bindingType) {
        String addrStr = null;

        switch (bindingType) {
            case ADDRESS:
                addrStr = this.toAddress();
                break;
            case DOMAIN:
                addrStr = this.getDomainNamePart();
                break;
            default:
                break;
        }
        return addrStr;
    }

    @Override
    public String[] toAddressParts() {
        return ArrayUtils.toArray(this.getLocalPart(), this.getDomainNamePart());
    }

    @Override
    @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
    public boolean equals(@Nullable Object obj) {
        return Objects.equals(Objects.toString(obj), this.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toString());
    }

    @Nullable
    @Override
    public String toString() {
        return this.toAddress();
    }

    @Override
    public boolean hasDomainName() {
        try {
            return this.getDomainName() != null;
        } catch (ToolMailAddressException ignored) {
        }

        return false;
    }

    @Nullable
    @Override
    public Name getDomainName() throws ToolMailAddressException {
        try {
            return ToolDnsNameUtils.fromString(this.getDomainNamePart());
        } catch (DnsNameException e) {
            throw new ToolMailAddressException(String.format("Unable to get mail address domain name from string: %s", this.getDomainNamePart()), e);
        }
    }

    @Override
    public void setDomainName(@Nullable Name domainName) {
        this.setDomainNamePart(Objects.toString(domainName));
    }

    @Override
    public boolean hasDomainNamePart() {
        return this.hasPart(MailAddressPart.DOMAIN_NAME);
    }

    @Nullable
    @Override
    public String getDomainNamePart() {
        return this.getPart(MailAddressPart.DOMAIN_NAME);
    }

    @Override
    public void setDomainNamePart(@Nullable String domainNamePart) {
        this.setPart(MailAddressPart.DOMAIN_NAME, domainNamePart);
    }

    @Override
    public boolean hasLocalPart() {
        return this.hasPart(MailAddressPart.LOCAL);
    }

    @Nullable
    @Override
    public String getLocalPart() {
        return this.getPart(MailAddressPart.LOCAL);
    }

    @Override
    public void setLocalPart(@Nullable String localPart) {
        this.setPart(MailAddressPart.LOCAL, localPart);
    }

    @Override
    public boolean hasPersonalPart() {
        return this.hasPart(MailAddressPart.PERSONAL);
    }

    @Nullable
    @Override
    public String getPersonalPart() {
        return this.getPart(MailAddressPart.PERSONAL);
    }

    @Override
    public void setPersonalPart(@Nullable String personalPart) {
        this.setPart(MailAddressPart.PERSONAL, personalPart);
    }

    @Override
    public boolean hasPart(MailAddressPart part) {
        return !StringUtils.isBlank(this.getPart(part));
    }

    @Nullable
    @Override
    public String getPart(MailAddressPart part) {
        return this.addrPartMap.get(part);
    }

    @Override
    public void setPart(MailAddressPart part, @Nullable String partValue) {
        this.addrPartMap.put(part, partValue);
    }
}
