package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils;
import java.util.regex.Pattern;
import org.apache.directory.api.ldap.model.filter.AssertionType;

public enum LdapAssertionType {
    PRESENCE(AssertionType.PRESENCE, "^" + ToolLdapAttributeUtils.PATTERN_STR_ATTR_ID + ToolLdapAttributeUtils.DELIM_VALUE + "\\s*\\*\\s*$"), EQUALITY(
        AssertionType.EQUALITY, "^" + ToolLdapAttributeUtils.PATTERN_STR_ATTR_ID + ToolLdapAttributeUtils.DELIM_VALUE + "\\s*"
            + ToolLdapAttributeUtils.PATTERN_STR_ATTR_VALUE + "\\s*$");

    private final AssertionType type;
    private final Pattern pattern;

    private LdapAssertionType(AssertionType type, String patternStr) {
        this.type = type;
        this.pattern = Pattern.compile(patternStr);
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public AssertionType getType() {
        return this.type;
    }
}
