package gov.hhs.onc.dcdt.ldap.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Value;

public abstract class ToolLdapAttributeUtils {
    public final static String DELIM = ":";
    public final static String DELIM_LINES = ToolLdifUtils.DELIM_LDIF_ENTRY;
    public final static String VALUE_EMPTY = "''";

    public static MutablePair<String, String[]> getStringParts(String ldapAttrStr) {
        String[] ldapAttrStrLines = getStringLines(ldapAttrStr), ldapAttrStrParts;
        String ldapAttrStrId = null;
        List<String> ldapAttrStrValues = new ArrayList<>(ldapAttrStrLines.length);

        for (String ldapAttrStrLine : ldapAttrStrLines) {
            ldapAttrStrParts =
                StringUtils.stripAll((ArrayUtils.getLength((ldapAttrStrParts = StringUtils.split(ldapAttrStrLine, DELIM, 2))) == 2)
                    ? ldapAttrStrParts : ArrayUtils.add(ldapAttrStrParts, VALUE_EMPTY));
            ldapAttrStrId = StringUtils.defaultIfEmpty(ldapAttrStrId, ldapAttrStrParts[0]);
            ldapAttrStrValues.add(StringUtils.defaultIfEmpty(ldapAttrStrParts[1], VALUE_EMPTY));
        }

        return new MutablePair<>(ldapAttrStrId, ldapAttrStrValues.toArray(new String[ldapAttrStrValues.size()]));
    }

    public static String[] getStringLines(String ldapAttrStr) {
        return StringUtils.split(ldapAttrStr, DELIM_LINES);
    }

    public static List<String> getValueStrings(Attribute ldapAttr) {
        List<String> ldapAttrValueStrs = new ArrayList<>(ldapAttr.size());

        for (Value<?> ldapAttrValue : ldapAttr) {
            ldapAttrValueStrs.add(ldapAttrValue.getString());
        }

        return ldapAttrValueStrs;
    }
}
