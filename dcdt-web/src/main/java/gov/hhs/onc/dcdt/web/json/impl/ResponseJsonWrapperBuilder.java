package gov.hhs.onc.dcdt.web.json.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
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

public class ResponseJsonWrapperBuilder<T extends ToolBean, U extends ToolBeanJsonDto<T>> implements Builder<ResponseJsonWrapper<T, U>> {
    private ResponseJsonWrapper<T, U> respJsonWrapper = new ResponseJsonWrapperImpl<>();

    @SuppressWarnings({ "unchecked" })
    public ResponseJsonWrapperBuilder<T, U> addItems(U ... itemsAdd) {
        return this.addItems(ToolArrayUtils.asList(itemsAdd));
    }

    public ResponseJsonWrapperBuilder<T, U> addItems(Collection<U> itemsAdd) {
        List<U> items;

        if ((items = this.respJsonWrapper.getItems()) == null) {
            this.respJsonWrapper.setItems(new ArrayList<>(itemsAdd));
        } else {
            items.addAll(itemsAdd);
        }

        return this;
    }

    public ResponseJsonWrapperBuilder<T, U> addBindingErrors(MessageSource msgSource, BindingResult bindingResult) {
        if (bindingResult.hasGlobalErrors()) {
            bindingResult.getGlobalErrors().forEach(globalErrorObj -> this.addGlobalErrors(new ErrorJsonWrapperImpl(ToolMessageUtils.getMessage(msgSource,
                globalErrorObj))));
        }

        if (bindingResult.hasFieldErrors()) {
            bindingResult.getFieldErrors().forEach(fieldErrorObj -> this.addFieldErrors(fieldErrorObj.getField(), new ErrorJsonWrapperImpl(ToolMessageUtils
                .getMessage(msgSource, fieldErrorObj))));
        }

        return this;
    }

    public ResponseJsonWrapperBuilder<T, U> addFieldErrors(String fieldName, ErrorJsonWrapper ... fieldErrorJsonWrappers) {
        return this.addFieldErrors(fieldName, ToolArrayUtils.asList(fieldErrorJsonWrappers));
    }

    public ResponseJsonWrapperBuilder<T, U> addFieldErrors(String fieldName, Collection<ErrorJsonWrapper> fieldErrorJsonWrappers) {
        this.getFieldErrorJsonWrappers(fieldName).addAll(fieldErrorJsonWrappers);

        return this.setStatus(ResponseStatus.ERROR);
    }

    public ResponseJsonWrapperBuilder<T, U> addGlobalErrorExceptions(Exception ... globalErrorExceptions) {
        return this.addGlobalErrorExceptions(ToolArrayUtils.asList(globalErrorExceptions));
    }

    public ResponseJsonWrapperBuilder<T, U> addGlobalErrorExceptions(Collection<Exception> globalErrorExceptions) {
        return this.addGlobalErrors(ToolStreamUtils.transform(globalErrorExceptions, ErrorJsonWrapperImpl::new));
    }

    public ResponseJsonWrapperBuilder<T, U> addGlobalErrors(ErrorJsonWrapper ... globalErrorJsonWrappers) {
        return this.addGlobalErrors(ToolArrayUtils.asList(globalErrorJsonWrappers));
    }

    public ResponseJsonWrapperBuilder<T, U> addGlobalErrors(Collection<ErrorJsonWrapper> globalErrorJsonWrappers) {
        this.getGlobalErrorJsonWrappers().addAll(globalErrorJsonWrappers);

        return this.setStatus(ResponseStatus.ERROR);
    }

    public ResponseJsonWrapperBuilder<T, U> setStatus(ResponseStatus status) {
        this.respJsonWrapper.setStatus(status);

        return this;
    }

    private List<ErrorJsonWrapper> getFieldErrorJsonWrappers(String fieldName) {
        Map<String, List<ErrorJsonWrapper>> fieldErrorJsonWrappersMap = this.getFieldErrorJsonWrappersMap();
        fieldErrorJsonWrappersMap.putIfAbsent(fieldName, new ArrayList<>());

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
    public ResponseJsonWrapper<T, U> build() {
        return this.respJsonWrapper;
    }
}
