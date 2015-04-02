package gov.hhs.onc.dcdt.web.tags.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.Serializer;
import org.htmlcleaner.TagNode;

@SuppressWarnings({ "serial" })
public class HtmlCleanerTag extends AbstractToolTag {
    private CleanerProperties cleanerProps;
    private HtmlCleaner cleaner;
    private Serializer cleanerSerializer;

    @Override
    protected int doAfterBodyInternal() throws Exception {
        TagNode bodyNode = this.cleaner.clean(this.bodyContent.getReader());

        this.bodyContent.clearBody();
        this.bodyContent.write(this.cleanerSerializer.getAsString(bodyNode, cleanerProps.getCharset()));
        this.bodyContent.writeOut(this.bodyContent.getEnclosingWriter());

        return super.doAfterBodyInternal();
    }

    @Override
    protected int doStartTagInternalWrapped() throws Exception {
        this.cleanerProps = ToolBeanFactoryUtils.getBeanOfType(this.appContext, CleanerProperties.class);
        this.cleaner = ToolBeanFactoryUtils.getBeanOfType(this.appContext, HtmlCleaner.class);
        this.cleanerSerializer = ToolBeanFactoryUtils.getBeanOfType(this.appContext, Serializer.class);

        return super.doStartTagInternalWrapped();
    }
}
