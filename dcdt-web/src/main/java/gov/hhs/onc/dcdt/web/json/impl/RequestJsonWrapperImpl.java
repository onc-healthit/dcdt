package gov.hhs.onc.dcdt.web.json.impl;


import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;

@JsonTypeName("request")
public class RequestJsonWrapperImpl<T extends ToolBean> extends AbstractJsonWrapper<T> implements RequestJsonWrapper<T> {
}
