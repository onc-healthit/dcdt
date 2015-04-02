package gov.hhs.onc.dcdt.web.tags.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.web.tags.ToolTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

@SuppressWarnings({ "serial" })
public abstract class AbstractToolTag extends RequestContextAwareTag implements ToolTag {
    protected AbstractApplicationContext appContext;
    protected BodyContent bodyContent;

    @Override
    public int doEndTag() throws JspException {
        try {
            return this.doEndTagInternal();
        } catch (Exception e) {
            throw new JspException(String.format("Unable to end JSP tag (class=%s).", ToolClassUtils.getName(this)), e);
        }
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            return this.doAfterBodyInternal();
        } catch (Exception e) {
            throw new JspException(String.format("Unable to post-process JSP tag (class=%s) body.", ToolClassUtils.getName(this)), e);
        }
    }

    @Override
    public void doInitBody() throws JspException {
        try {
            this.doInitBodyInternal();
        } catch (Exception e) {
            throw new JspException(String.format("Unable to initialize JSP tag (class=%s) body.", ToolClassUtils.getName(this)), e);
        }
    }

    protected int doEndTagInternal() throws Exception {
        return EVAL_PAGE;
    }

    protected int doAfterBodyInternal() throws Exception {
        return SKIP_BODY;
    }

    protected void doInitBodyInternal() throws Exception {
    }

    @Override
    protected int doStartTagInternal() throws Exception {
        this.appContext = ((AbstractApplicationContext) this.getRequestContext().getWebApplicationContext());

        try {
            return this.doStartTagInternalWrapped();
        } catch (Exception e) {
            throw new JspException(String.format("Unable to start JSP tag (class=%s).", ToolClassUtils.getName(this)), e);
        }
    }

    protected int doStartTagInternalWrapped() throws Exception {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }
}
