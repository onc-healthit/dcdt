package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.web.json.impl.RequestJsonWrapperImpl;

@JsonSubTypes({ @Type(RequestJsonWrapperImpl.class) })
public interface RequestJsonWrapper<T extends ToolBean, U extends ToolBeanJsonDto<T>> extends JsonWrapper<T, U> {
}
