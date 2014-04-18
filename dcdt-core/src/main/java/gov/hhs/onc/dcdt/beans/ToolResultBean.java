package gov.hhs.onc.dcdt.beans;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import java.util.List;

public interface ToolResultBean extends ToolBean {
    public static class ToolResultBeanMessageExtractor extends AbstractToolTransformer<ToolResultBean, List<String>> {
        public final static ToolResultBeanMessageExtractor INSTANCE = new ToolResultBeanMessageExtractor();

        @Override
        protected List<String> transformInternal(ToolResultBean resultBean) throws Exception {
            return resultBean.getMessages();
        }
    }

    public boolean hasMessages();

    public List<String> getMessages();

    public boolean isSuccess();
}
