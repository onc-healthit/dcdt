package gov.hhs.onc.dcdt.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    public static Map<String, List<String>> mapErrorMessages(MessageSource msgSource, Errors errors) {
        Map<String, List<String>> errorMsgsMap = new LinkedHashMap<>();

        if (errors.hasGlobalErrors()) {
            errorMsgsMap.put(null, getErrorMessages(msgSource, errors.getGlobalErrors()));
        }

        if (errors.hasFieldErrors()) {
            String fieldName;

            for (FieldError fieldError : errors.getFieldErrors()) {
                if (!errorMsgsMap.containsKey(fieldName = fieldError.getField())) {
                    errorMsgsMap.put(fieldName, new ArrayList<String>());
                }

                errorMsgsMap.get(fieldName).add(getErrorMessage(msgSource, fieldError));
            }
        }

        return errorMsgsMap;
    }

    public static List<String> getErrorMessages(MessageSource msgSource, Iterable<ObjectError> errors) {
        List<String> errorMsgs = new ArrayList<>();

        for (ObjectError error : errors) {
            errorMsgs.add(getErrorMessage(msgSource, error));
        }

        return errorMsgs;
    }

    public static String getErrorMessage(MessageSource msgSource, ObjectError error) {
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
