package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.ldap.LdapAttribute;
import org.apache.directory.api.ldap.model.entry.StringValue;
import org.apache.directory.api.ldap.model.filter.EqualityNode;

public class MailAddressEqualityNode extends EqualityNode<String> {
    public MailAddressEqualityNode(String mailAddr) {
        super(LdapAttribute.MAIL.getName(), new StringValue(mailAddr));
    }
}
