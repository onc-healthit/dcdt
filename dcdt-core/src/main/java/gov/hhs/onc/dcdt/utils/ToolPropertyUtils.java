package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolPropertyUtils {
    public final static String DELIM_PROP_NAME = ".";
    public final static String DELIM_PROP_VALUES = ",";
    public final static String DELIM_PROP_VALUES_ESCAPED = "\\" + DELIM_PROP_VALUES;

    public static String joinName(String ... nameParts) {
        return joinName(ToolArrayUtils.asList(nameParts));
    }

    public static String joinName(Iterable<String> nameParts) {
        return ToolStringUtils.joinDelimit(nameParts, DELIM_PROP_NAME);
    }

    public static String joinValues(@Nullable String ... values) {
        return joinValues(ToolArrayUtils.asList(values));
    }

    public static String joinValues(@Nullable Collection<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return StringUtils.EMPTY;
        }

        List<String> valuesEscaped = new ArrayList<>();

        // noinspection ConstantConditions
        for (String value : values) {
            valuesEscaped.add(StringUtils.replace(value, DELIM_PROP_VALUES, DELIM_PROP_VALUES_ESCAPED));
        }

        return ToolStringUtils.joinDelimit(valuesEscaped, DELIM_PROP_VALUES);
    }
}
