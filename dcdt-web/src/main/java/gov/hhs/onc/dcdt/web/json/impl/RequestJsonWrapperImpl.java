package gov.hhs.onc.dcdt.web.json.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.RequestJsonWrapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("requestJsonWrapper")
@JsonTypeName("request")
@Lazy
@Scope("prototype")
public class RequestJsonWrapperImpl<T extends ToolBean> extends AbstractJsonWrapper<T> implements RequestJsonWrapper<T> {
}
