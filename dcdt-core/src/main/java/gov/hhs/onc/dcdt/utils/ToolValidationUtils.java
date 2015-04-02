package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

public abstract class ToolValidationUtils {
    private final static String MSG_MACRO_ESCAPE = "\\";
    private final static String PATTERN_STR_MSG_MACRO_ESCAPE = Pattern.quote(MSG_MACRO_ESCAPE);
    private final static String PATTERN_STR_MSG_MACRO_AFFIX = "(?!" + PATTERN_STR_MSG_MACRO_ESCAPE + ")(\\$?\\{|\\})";

    private final static String REPLACEMENT_STR_MSG_MACRO_ESCAPE = Matcher.quoteReplacement(MSG_MACRO_ESCAPE) + "$1";

    private final static Pattern PATTERN_MSG_MACRO_AFFIX = Pattern.compile(PATTERN_STR_MSG_MACRO_AFFIX);

    public static String escapeMessageMacros(String msg) {
        return PATTERN_MSG_MACRO_AFFIX.matcher(msg).replaceAll(REPLACEMENT_STR_MSG_MACRO_ESCAPE);
    }

    public static Map<String, List<String>> mapErrorMessages(MessageSource msgSource, Errors errors) {
        Map<String, List<String>> errorMsgsMap = new LinkedHashMap<>();

        if (errors.hasGlobalErrors()) {
            errorMsgsMap.put(null, buildErrorMessages(msgSource, errors.getGlobalErrors()));
        }

        if (errors.hasFieldErrors()) {
            String fieldName;

            for (FieldError fieldError : errors.getFieldErrors()) {
                errorMsgsMap.putIfAbsent(fieldName = fieldError.getField(), new ArrayList<>());
                errorMsgsMap.get(fieldName).add(buildErrorMessage(msgSource, fieldError));
            }
        }

        return errorMsgsMap;
    }

    public static List<String> buildErrorMessages(MessageSource msgSource, Iterable<ObjectError> errors) {
        return (List<String>) ToolStreamUtils.transform(errors, error -> buildErrorMessage(msgSource, error));
    }

    public static String buildErrorMessage(MessageSource msgSource, ObjectError error) {
        return ToolMessageUtils.getMessage(msgSource, error);
    }

    public static BindingResult bind(Validator validator, Object obj, Object ... validationHints) {
        return bind(validator, obj, null, validationHints);
    }

    public static BindingResult bind(Validator validator, Object obj, @Nullable String objName, Object ... validationHints) {
        DataBinder dataBinder = new DataBinder(obj, ObjectUtils.defaultIfNull(objName, DataBinder.DEFAULT_OBJECT_NAME));
        dataBinder.setValidator(validator);

        return bind(dataBinder, validationHints);
    }

    public static BindingResult bind(DataBinder dataBinder, Object ... validationHints) {
        dataBinder.validate(validationHints);

        return dataBinder.getBindingResult();
    }
}
