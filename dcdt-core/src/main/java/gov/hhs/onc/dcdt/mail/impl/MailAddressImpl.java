package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.dns.DnsNameException;
import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.BindingType;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailAddressException;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.xbill.DNS.Name;

public class MailAddressImpl extends AbstractToolBean implements MailAddress {
    private MutablePair<String, String> addrPartsPair = new MutablePair<>();

    public MailAddressImpl() {
        this(null, null);
    }

    public MailAddressImpl(@Nullable String addr) {
        String[] addrParts = ToolMailAddressUtils.splitParts(addr);

        this.setLocalPart(ToolArrayUtils.getFirst(addrParts));
        this.setDomainNamePart(ToolArrayUtils.getLast(addrParts));
    }

    public MailAddressImpl(@Nullable String localPart, @Nullable String domainNamePart) {
        this.setLocalPart(localPart);
        this.setDomainNamePart(domainNamePart);
    }

    @Nullable
    @Override
    public InternetAddress toInternetAddress() throws ToolMailAddressException {
        String addr = this.toAddress();

        if (StringUtils.isBlank(addr)) {
            return null;
        }

        try {
            return new InternetAddress(addr, true);
        } catch (AddressException e) {
            throw new ToolMailAddressException(String.format("Unable to get Internet mail address from mail address string: %s", addr), e);
        }
    }

    @Nullable
    @Override
    public Name toDnsName() throws ToolMailAddressException {
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
    public String toAddress() {
        return ToolMailAddressUtils.joinParts(this.toAddressParts());
    }

    @Override
    public String[] toAddressParts() {
        return ArrayUtils.toArray(this.getLocalPart(), this.getDomainNamePart());
    }

    @Override
    public BindingType getBindingType() {
        return this.hasDomainNamePart() ? (this.hasLocalPart() ? BindingType.ADDRESS : BindingType.DOMAIN) : BindingType.NONE;
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
        return !StringUtils.isBlank(this.getDomainNamePart());
    }

    @Nullable
    @Override
    public String getDomainNamePart() {
        return this.addrPartsPair.getRight();
    }

    @Override
    public void setDomainNamePart(@Nullable String domainNamePart) {
        this.addrPartsPair.setRight(domainNamePart);
    }

    @Override
    public boolean hasLocalPart() {
        return !StringUtils.isBlank(this.getLocalPart());
    }

    @Nullable
    @Override
    public String getLocalPart() {
        return this.addrPartsPair.getLeft();
    }

    @Override
    public void setLocalPart(@Nullable String localPart) {
        this.addrPartsPair.setLeft(localPart);
    }
}
