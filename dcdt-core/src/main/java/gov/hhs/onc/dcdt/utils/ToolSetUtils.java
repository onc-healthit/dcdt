package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ToolSetUtils {
    public static <T> List<T> toList(Set<T> set) {
        return new ArrayList<>(set);
    }
}
