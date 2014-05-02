package gov.hhs.onc.dcdt.web.tags.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.json.utils.ToolJsonUtils;

@SuppressWarnings({ "serial" })
public class JsonTag extends AbstractToolTag {
    private ObjectMapper objMapper;
    private Object target;

    @Override
    protected int doEndTagInternal() throws Exception {
        this.pageContext.getOut().println(ToolJsonUtils.toJson(this.objMapper, this.target));

        return super.doEndTagInternal();
    }

    @Override
    protected int doStartTagInternalWrapped() throws Exception {
        this.objMapper = ToolBeanFactoryUtils.getBeanOfType(this.appContext, ObjectMapper.class);

        return super.doStartTagInternalWrapped();
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
