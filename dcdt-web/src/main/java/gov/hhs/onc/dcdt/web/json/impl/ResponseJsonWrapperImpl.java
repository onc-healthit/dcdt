package gov.hhs.onc.dcdt.web.json.impl;


import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.ErrorJsonWrapper;
import gov.hhs.onc.dcdt.web.json.ResponseJsonWrapper;
import java.util.ArrayList;
import java.util.List;

@JsonTypeName("response")
public class ResponseJsonWrapperImpl<T extends ToolBean> extends AbstractJsonWrapper<T> implements ResponseJsonWrapper<T> {
    private List<ErrorJsonWrapper> errors = new ArrayList<>();

    @Override
    public List<ErrorJsonWrapper> getErrors() {
        return this.errors;
    }

    @Override
    public void setErrors(List<ErrorJsonWrapper> errors) {
        this.errors = errors;
    }
}
