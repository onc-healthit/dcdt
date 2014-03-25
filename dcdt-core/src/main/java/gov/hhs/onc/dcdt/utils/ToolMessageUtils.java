package gov.hhs.onc.dcdt.utils;

import java.util.Locale;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public abstract class ToolMessageUtils {
    @SuppressWarnings({ "NullArgumentToVariableArgMethod" })
    public static String getMessage(MessageSource msgSource, String code) throws NoSuchMessageException {
        return getMessage(msgSource, code, null);
    }

    public static String getMessage(MessageSource msgSource, String code, @Nullable Object ... msgArgs) throws NoSuchMessageException {
        return getMessage(msgSource, code, null, msgArgs);
    }

    @SuppressWarnings({ "NullArgumentToVariableArgMethod" })
    public static String getMessage(MessageSource msgSource, String code, @Nullable String defaultMsg) throws NoSuchMessageException {
        return getMessage(msgSource, code, defaultMsg, null);
    }

    public static String getMessage(MessageSource msgSource, String code, @Nullable String defaultMsg, @Nullable Object ... msgArgs)
        throws NoSuchMessageException {
        return getMessage(msgSource, code, defaultMsg, null, msgArgs);
    }

    @SuppressWarnings({ "NullArgumentToVariableArgMethod" })
    public static String getMessage(MessageSource msgSource, String code, @Nullable String defaultMsg, @Nullable Locale msgLocale)
        throws NoSuchMessageException {
        return getMessage(msgSource, code, defaultMsg, msgLocale, (Object[]) null);
    }

    public static String
        getMessage(MessageSource msgSource, String code, @Nullable String defaultMsg, @Nullable Locale msgLocale, @Nullable Object ... msgArgs)
            throws NoSuchMessageException {
        return getMessage(msgSource, new DefaultMessageSourceResolvable(ArrayUtils.toArray(code), ToolArrayUtils.emptyToNull(msgArgs), defaultMsg), msgLocale);
    }

    public static String getMessage(MessageSource msgSource, MessageSourceResolvable msgResolvable) throws NoSuchMessageException {
        return getMessage(msgSource, msgResolvable, null);
    }

    public static String getMessage(MessageSource msgSource, MessageSourceResolvable msgResolvable, @Nullable Locale msgLocale) throws NoSuchMessageException {
        return msgSource.getMessage(msgResolvable, msgLocale);
    }
}
