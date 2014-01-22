package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.ldap.utils.ToolLdapUtils;
import java.util.regex.Pattern;
import org.apache.directory.api.ldap.model.filter.AssertionType;

public enum LdapAssertionType {
    EQUALITY(AssertionType.EQUALITY, Pattern.compile("^\\(" + ToolLdapUtils.PATTERN_STR_ATTR + "=([^$]+)\\)$")), PRESENCE(AssertionType.PRESENCE, Pattern
        .compile("^\\(" + ToolLdapUtils.PATTERN_STR_ATTR + "=\\*\\)$"));

    private final AssertionType type;
    private final Pattern pattern;

    private LdapAssertionType(AssertionType type, Pattern pattern) {
        this.type = type;
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public AssertionType getType() {
        return this.type;
    }
}
