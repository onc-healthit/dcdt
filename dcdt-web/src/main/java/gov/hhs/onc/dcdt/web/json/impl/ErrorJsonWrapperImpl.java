package gov.hhs.onc.dcdt.web.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("errorJsonWrapperImpl")
@JsonTypeName("error")
@Lazy
@Scope("prototype")
public class ErrorJsonWrapperImpl extends AbstractToolBean implements ErrorJsonWrapper {
    private List<String> msgs = new ArrayList<>();
    private String stackTrace;

    public ErrorJsonWrapperImpl() {
        this(ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public ErrorJsonWrapperImpl(Throwable error) {
        this(error.getMessage(), ToolStringUtils.joinDelimit(ExceptionUtils.getRootCauseStackTrace(error), "\n"));
    }

    public ErrorJsonWrapperImpl(String ... msgs) {
        this(ToolArrayUtils.asList(msgs));
    }

    public ErrorJsonWrapperImpl(List<String> msgs) {
        this(msgs, null);
    }

    public ErrorJsonWrapperImpl(String msg, @Nullable String stackTrace) {
        this(ToolArrayUtils.asList(msg), stackTrace);
    }

    public ErrorJsonWrapperImpl(List<String> msgs, @Nullable String stackTrace) {
        this.msgs = msgs;
        this.stackTrace = stackTrace;
    }

    @Override
    public boolean hasMessages() {
        return !ToolCollectionUtils.isEmpty(this.msgs);
    }

    @Override
    public List<String> getMessages() {
        return this.msgs;
    }

    @Override
    public void setMessages(List<String> msgs) {
        this.msgs = msgs;
    }

    @Override
    public boolean hasStackTrace() {
        return !StringUtils.isBlank(this.stackTrace);
    }

    @Override
    public String getStackTrace() {
        return this.stackTrace;
    }

    @Override
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
