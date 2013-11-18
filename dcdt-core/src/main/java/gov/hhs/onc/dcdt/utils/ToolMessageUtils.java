package gov.hhs.onc.dcdt.utils;


import java.util.Locale;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;

public abstract class ToolMessageUtils {
    public static String getMessage(MessageSource msgSource, String code, Object ... msgArgs) throws NoSuchMessageException {
        return getMessage(msgSource, code, null, msgArgs);
    }

    public static String getMessage(MessageSource msgSource, String code, String defaultMsg, Object ... msgArgs)
        throws NoSuchMessageException {
        return getMessage(msgSource, code, defaultMsg, null, msgArgs);
    }
    
    public static String getMessage(MessageSource msgSource, String code, String defaultMsg, Locale msgLocale, Object ... msgArgs)
        throws NoSuchMessageException {
        return getMessage(msgSource, new DefaultMessageSourceResolvable(ArrayUtils.toArray(code), ToolArrayUtils.emptyToNull(msgArgs), defaultMsg), msgLocale);
    }

    public static String getMessage(MessageSource msgSource, MessageSourceResolvable msgResolvable) throws NoSuchMessageException {
        return getMessage(msgSource, msgResolvable, null);
    }

    public static String getMessage(MessageSource msgSource, MessageSourceResolvable msgResolvable, Locale msgLocale) throws NoSuchMessageException {
        return msgSource.getMessage(msgResolvable, msgLocale);
    }
}
