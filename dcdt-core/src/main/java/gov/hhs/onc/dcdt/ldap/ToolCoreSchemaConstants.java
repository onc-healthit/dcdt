package gov.hhs.onc.dcdt.ldap;

import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.DefaultAttribute;

public final class ToolCoreSchemaConstants {
    private ToolCoreSchemaConstants() {
    }

    public final static String ATTR_TYPE_OID_MAIL = "0.9.2342.19200300.100.1.3";
    public final static String ATTR_TYPE_NAME_MAIL = "mail";
    public final static Attribute ATTR_MAIL = new DefaultAttribute(ATTR_TYPE_NAME_MAIL);

    public final static String ATTR_TYPE_OID_USER_CERT = "2.5.4.36";
    public final static String ATTR_TYPE_NAME_USER_CERT = "userCertificate";
    public final static Attribute ATTR_USER_CERT = new DefaultAttribute(ATTR_TYPE_NAME_USER_CERT);
}
