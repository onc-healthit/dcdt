package gov.hhs.onc.dcdt.beans.factory.xml.impl;

import gov.hhs.onc.dcdt.beans.factory.impl.ToolPropertiesFactoryBean;
import gov.hhs.onc.dcdt.beans.factory.xml.ToolNamespaceHandler;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ToolNamespaceHandlerImpl extends NamespaceHandlerSupport implements ToolNamespaceHandler {
    private final static class ToolPropertiesBeanDefinitionParser extends AbstractToolBeanDefinitionParser<ToolPropertiesFactoryBean> {
        public final static ToolPropertiesBeanDefinitionParser INSTANCE = new ToolPropertiesBeanDefinitionParser();

        private final static String ATTR_NAME_PROPS = "properties";

        private ToolPropertiesBeanDefinitionParser() {
            super(ToolPropertiesFactoryBean.class, "properties");
        }

        @Override
        protected void doParse(Element elem, ParserContext parserContext, BeanDefinitionBuilder beanDefBuilder) {
            super.doParse(elem, parserContext, beanDefBuilder);

            beanDefBuilder.addPropertyValue(ATTR_NAME_PROPS, parserContext.getDelegate().parsePropsElement(elem));
        }
    }

    @Override
    public void init() {
        this.registerBeanDefinitionParser(ToolPropertiesBeanDefinitionParser.INSTANCE.getElementName(), ToolPropertiesBeanDefinitionParser.INSTANCE);
    }
}
