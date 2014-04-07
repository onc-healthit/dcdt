package gov.hhs.onc.dcdt.web.tags.functions.impl;

import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class StringTagFunctions extends AbstractToolTagFunctions {
    public static @Nullable String concat(@Nullable Object obj1, @Nullable Object obj2) {
        return (obj1 != null) ? (Objects.toString(obj1) + ((obj2 != null) ? Objects.toString(obj2) : StringUtils.EMPTY)) : Objects.toString(obj2, null);
    }
}
