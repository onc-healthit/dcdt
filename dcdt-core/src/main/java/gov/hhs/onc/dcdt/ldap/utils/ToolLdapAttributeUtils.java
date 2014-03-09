package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;

public abstract class ToolLdapAttributeUtils {
    public static class LdapAttributeIdTransformer implements Transformer<Attribute, String> {
        public final static LdapAttributeIdTransformer INSTANCE = new LdapAttributeIdTransformer();
        public final static LdapAttributeIdTransformer INSTANCE_USER_PROVIDED = new LdapAttributeIdTransformer(true);

        private boolean userProvided;

        public LdapAttributeIdTransformer() {
            this(false);
        }

        public LdapAttributeIdTransformer(boolean userProvided) {
            this.userProvided = userProvided;
        }

        @Override
        public String transform(Attribute attr) {
            return (this.userProvided ? attr.getUpId() : attr.getId());
        }
    }

    public final static String DELIM_ATTR = ":";
    public final static String DELIM_ATTR_BINARY = StringUtils.repeat(DELIM_ATTR, 2);

    public static Attribute readAttribute(String str) throws ToolLdapException {
        LdifReader attrLdifReader = new LdifReader();
        LdifEntry attrLdifEntry = new LdifEntry();

        // noinspection ConstantConditions
        for (String strLine : ToolStringUtils.splitLines(str)) {
            try {
                attrLdifReader.parseAttributeValue(attrLdifEntry, strLine, strLine.toLowerCase());
            } catch (LdapException e) {
                throw new ToolLdapException(String.format("Unable to read LDAP attribute from LDIF string line: %s", strLine), e);
            }
        }

        return attrLdifEntry.getEntry().iterator().next();
    }

    public static String writeAttribute(Attribute attr) throws ToolLdapException {
        try {
            return LdifUtils.convertToLdif(attr);
        } catch (LdapException e) {
            throw new ToolLdapException(String.format("Unable to write LDAP attribute (upId={%s}, values=[%s]) to LDIF string.", attr.getUpId(),
                ToolStringUtils.joinDelimit(attr, ", ")), e);
        }
    }
}
