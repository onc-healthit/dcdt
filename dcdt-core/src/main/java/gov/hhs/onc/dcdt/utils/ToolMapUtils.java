package gov.hhs.onc.dcdt.utils;

import java.util.Map;
import javax.annotation.Nullable;

public abstract class ToolMapUtils {
    public static <T, U> boolean isEmpty(@Nullable Map<T, U> map) {
        return (map == null) || map.isEmpty();
    }
}
