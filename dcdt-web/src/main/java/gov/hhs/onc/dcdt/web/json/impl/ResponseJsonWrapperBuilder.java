package gov.hhs.onc.dcdt.web.json.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ErrorsJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.Builder;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ResponseJsonWrapperBuilder<T extends ToolBean> implements Builder<ResponseJsonWrapper<T>> {
    private ResponseJsonWrapper<T> respJsonWrapper = new ResponseJsonWrapperImpl<>();

    @SuppressWarnings({ "unchecked" })
    public ResponseJsonWrapperBuilder<T> addItems(T ... itemsAdd) {
        return this.addItems(ToolArrayUtils.asList(itemsAdd));
    }

    public ResponseJsonWrapperBuilder<T> addItems(Collection<T> itemsAdd) {
        List<T> items;

        if ((items = this.respJsonWrapper.getItems()) == null) {
            this.respJsonWrapper.setItems(new ArrayList<>(itemsAdd));
        } else {
            items.addAll(itemsAdd);
        }

        return this;
    }

    public ResponseJsonWrapperBuilder<T> addBindingErrors(MessageSource msgSource, BindingResult bindingResult) {
        if (bindingResult.hasGlobalErrors()) {
            for (ObjectError globalErrorObj : bindingResult.getGlobalErrors()) {
                this.addGlobalErrors(new ErrorJsonWrapperImpl(ToolMessageUtils.getMessage(msgSource, globalErrorObj)));
            }
        }

        if (bindingResult.hasFieldErrors()) {
            for (FieldError fieldErrorObj : bindingResult.getFieldErrors()) {
                this.addFieldErrors(fieldErrorObj.getField(), new ErrorJsonWrapperImpl(ToolMessageUtils.getMessage(msgSource, fieldErrorObj)));
            }
        }

        return this;
    }

    public ResponseJsonWrapperBuilder<T> addFieldErrors(String fieldName, ErrorJsonWrapper ... fieldErrorJsonWrappers) {
        return this.addFieldErrors(fieldName, ToolArrayUtils.asList(fieldErrorJsonWrappers));
    }

    public ResponseJsonWrapperBuilder<T> addFieldErrors(String fieldName, Collection<ErrorJsonWrapper> fieldErrorJsonWrappers) {
        this.getFieldErrorJsonWrappers(fieldName).addAll(fieldErrorJsonWrappers);

        return this.setStatus(ResponseStatus.ERROR);
    }

    public ResponseJsonWrapperBuilder<T> addGlobalErrorExceptions(Exception ... globalErrorExceptions) {
        return this.addGlobalErrorExceptions(ToolArrayUtils.asList(globalErrorExceptions));
    }

    public ResponseJsonWrapperBuilder<T> addGlobalErrorExceptions(Collection<Exception> globalErrorExceptions) {
        List<ErrorJsonWrapper> globalErrorJsonWrappers = new ArrayList<>(globalErrorExceptions.size());

        for (Exception globalErrorException : globalErrorExceptions) {
            globalErrorJsonWrappers.add(new ErrorJsonWrapperImpl(globalErrorException));
        }

        return this.addGlobalErrors(globalErrorJsonWrappers);
    }

    public ResponseJsonWrapperBuilder<T> addGlobalErrors(ErrorJsonWrapper ... globalErrorJsonWrappers) {
        return this.addGlobalErrors(ToolArrayUtils.asList(globalErrorJsonWrappers));
    }

    public ResponseJsonWrapperBuilder<T> addGlobalErrors(Collection<ErrorJsonWrapper> globalErrorJsonWrappers) {
        this.getGlobalErrorJsonWrappers().addAll(globalErrorJsonWrappers);

        return this.setStatus(ResponseStatus.ERROR);
    }

    public ResponseJsonWrapperBuilder<T> setStatus(ResponseStatus status) {
        this.respJsonWrapper.setStatus(status);

        return this;
    }

    private List<ErrorJsonWrapper> getFieldErrorJsonWrappers(String fieldName) {
        Map<String, List<ErrorJsonWrapper>> fieldErrorJsonWrappersMap = this.getFieldErrorJsonWrappersMap();

        if (!fieldErrorJsonWrappersMap.containsKey(fieldName)) {
            fieldErrorJsonWrappersMap.put(fieldName, new ArrayList<ErrorJsonWrapper>());
        }

        return fieldErrorJsonWrappersMap.get(fieldName);
    }

    private Map<String, List<ErrorJsonWrapper>> getFieldErrorJsonWrappersMap() {
        ErrorsJsonWrapper errorsJsonWrapper;
        Map<String, List<ErrorJsonWrapper>> fieldErrorJsonWrappersMap;

        if ((fieldErrorJsonWrappersMap = (errorsJsonWrapper = this.getErrorsJsonWrapper()).getFieldErrors()) == null) {
            errorsJsonWrapper.setFieldErrors((fieldErrorJsonWrappersMap = new LinkedHashMap<>()));
        }

        return fieldErrorJsonWrappersMap;
    }

    private List<ErrorJsonWrapper> getGlobalErrorJsonWrappers() {
        ErrorsJsonWrapper errorsJsonWrapper;
        List<ErrorJsonWrapper> globalErrorJsonWrappers;

        if ((globalErrorJsonWrappers = (errorsJsonWrapper = this.getErrorsJsonWrapper()).getGlobalErrors()) == null) {
            errorsJsonWrapper.setGlobalErrors((globalErrorJsonWrappers = new ArrayList<>()));
        }

        return globalErrorJsonWrappers;
    }

    private ErrorsJsonWrapper getErrorsJsonWrapper() {
        ErrorsJsonWrapper errorsJsonWrapper;

        if ((errorsJsonWrapper = this.respJsonWrapper.getErrors()) == null) {
            this.respJsonWrapper.setErrors((errorsJsonWrapper = new ErrorsJsonWrapperImpl()));
        }

        return errorsJsonWrapper;
    }

    @Override
    public ResponseJsonWrapper<T> build() {
        return this.respJsonWrapper;
    }
}
