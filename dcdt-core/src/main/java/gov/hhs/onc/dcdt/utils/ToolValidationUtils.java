package gov.hhs.onc.dcdt.utils;

import javax.annotation.Nullable;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

public abstract class ToolValidationUtils {
    public static BindingResult bind(Validator validator, Object obj) {
        return bind(validator, obj, null);
    }

    public static BindingResult bind(Validator validator, Object obj, @Nullable String objName) {
        DataBinder dataBinder = new DataBinder(obj, ObjectUtils.defaultIfNull(objName, DataBinder.DEFAULT_OBJECT_NAME));
        dataBinder.setValidator(validator);

        return bind(dataBinder);
    }

    public static BindingResult bind(DataBinder dataBinder) {
        dataBinder.validate();

        return dataBinder.getBindingResult();
    }
}
