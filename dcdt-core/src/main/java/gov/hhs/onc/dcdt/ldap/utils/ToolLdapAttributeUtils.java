package gov.hhs.onc.dcdt.ldap.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Value;

public abstract class ToolLdapAttributeUtils {
    public final static String DELIM = ":";
    public final static String DELIM_BINARY = StringUtils.repeat(DELIM, 2);
    public final static String DELIM_VALUE = "=";
    public final static String DELIM_LINES = ToolLdifUtils.DELIM_LDIF_ENTRY;
    public final static String VALUE_EMPTY = "''";
    public final static String VALUE_EMPTY_BINARY = StringUtils.EMPTY;

    public final static String PATTERN_STR_ATTR_ID = "((?:(?:\\d|[1-9]\\d*)(?:\\.(?:\\d|[1-9]\\d*))+)|(?:[a-zA-Z][a-zA-Z0-9-]*))";
    public final static String PATTERN_STR_ATTR_VALUE = "([^$]+)";
    public final static String PATTERN_STR_ATTR_BEFORE = "^(\\()?\\s*";
    public final static String PATTERN_STR_ATTR_AFTER = "\\s*(\\))?$";
    public final static Pattern PATTERN_ATTR_DELIM = Pattern.compile(",\\s*");
    public final static Pattern PATTERN_ATTR_LINE = Pattern.compile("^" + PATTERN_STR_ATTR_ID + "\\s*(?:" + DELIM_BINARY + "\\s*(?:" + PATTERN_STR_ATTR_VALUE
        + "|" + VALUE_EMPTY_BINARY + ")|" + DELIM + "\\s*(?:" + PATTERN_STR_ATTR_VALUE + "|" + VALUE_EMPTY + "))\\s*$");

    public static String[] buildCaseInsensitiveIds(Iterable<Attribute> ldapAttrs) {
        Set<String> ldapAttrIds = new LinkedHashSet<>();
        String ldapAttrId;

        for (Attribute ldapAttr : ldapAttrs) {
            ldapAttrIds.add((ldapAttrId = ldapAttr.getUpId()));
            ldapAttrIds.add(ldapAttrId.toLowerCase());
        }

        return ldapAttrIds.toArray(new String[ldapAttrIds.size()]);
    }

    public static MutablePair<String, String[]> getStringParts(String ldapAttrStr) {
        String[] ldapAttrStrLines = getStringLines(ldapAttrStr);
        String ldapAttrStrId = null;
        List<String> ldapAttrStrValues = new ArrayList<>(ldapAttrStrLines.length);
        Matcher ldapAttrStrLineMatcher;

        for (String ldapAttrStrLine : ldapAttrStrLines) {
            if ((ldapAttrStrLineMatcher = PATTERN_ATTR_LINE.matcher(ldapAttrStrLine)).matches()) {
                ldapAttrStrId = StringUtils.defaultIfEmpty(ldapAttrStrId, ldapAttrStrLineMatcher.group(1));

                if (ldapAttrStrLineMatcher.groupCount() == 2) {
                    ldapAttrStrValues.add(ldapAttrStrLineMatcher.group(2));
                }
            }
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
