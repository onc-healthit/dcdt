package gov.hhs.onc.dcdt.web.tags.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.dcdt.json.ToolJsonException;
import gov.hhs.onc.dcdt.json.utils.ToolJsonUtils;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import javax.servlet.jsp.JspException;

@SuppressWarnings({ "serial" })
public class JsonTag extends AbstractToolTag {
    private ObjectMapper objMapper;
    private Object target;

    @Override
    public int doEndTag() throws JspException {
        try {
            this.pageContext.getOut().println(ToolJsonUtils.toJson(this.objMapper, this.target));
        } catch (IOException | ToolJsonException e) {
            throw new JspException(String.format("Unable to write target (class=%s) JSON.", ToolClassUtils.getName(this.target)), e);
        }

        return super.doEndTag();
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        this.objMapper = ToolBeanFactoryUtils.getBeanOfType(this.getRequestContext().getWebApplicationContext(), ObjectMapper.class);

        return super.doStartTagInternal();
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
