package gov.hhs.onc.dcdt.utils;


import org.apache.commons.lang3.StringUtils;

public abstract class ToolPropertyUtils {
    public final static String PROP_NAME_DELIM = ".";

    public static String joinName(String ... propNameParts) {
        return ToolStringUtils.joinDelimit(propNameParts, PROP_NAME_DELIM);
    }

    public static String joinName(Iterable<String> propNameParts) {
        return ToolStringUtils.joinDelimit(propNameParts, PROP_NAME_DELIM);
    }

    public static String[] splitName(String propName) {
        return StringUtils.split(propName, PROP_NAME_DELIM);
    }
}
