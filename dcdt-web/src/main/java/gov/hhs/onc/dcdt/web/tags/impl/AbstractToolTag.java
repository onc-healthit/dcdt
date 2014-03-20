package gov.hhs.onc.dcdt.web.tags.impl;

import gov.hhs.onc.dcdt.web.tags.ToolTag;
import javax.servlet.jsp.JspException;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

public abstract class AbstractToolTag extends RequestContextAwareTag implements ToolTag {
    @Override
    protected int doStartTagInternal() throws Exception {
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        // TODO: implement
    }
}
