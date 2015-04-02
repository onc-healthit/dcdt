package gov.hhs.onc.dcdt.beans.factory.xml.impl;

import gov.hhs.onc.dcdt.beans.factory.xml.ToolBeanDefinitionParser;
import gov.hhs.onc.dcdt.beans.factory.xml.ToolNamespaceHandler;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ToolNamespaceHandlerImpl extends NamespaceHandlerSupport implements ToolNamespaceHandler {
    private static class NormalizedStringBeanDefinitionParser extends AbstractToolBeanDefinitionParser<String> {
        public NormalizedStringBeanDefinitionParser() {
            super(String.class, "normalized-string");
        }

        @Override
        protected void doParse(Element elem, ParserContext parserContext, BeanDefinitionBuilder beanDefBuilder) {
            beanDefBuilder.addConstructorArgValue(StringUtils.normalizeSpace(elem.getTextContent()));

            super.doParse(elem, parserContext, beanDefBuilder);
        }
    }

    @SuppressWarnings({ "unchecked" })
    private final static ToolBeanDefinitionParser<?>[] BEAN_DEF_PARSERS = ArrayUtils.toArray(new NormalizedStringBeanDefinitionParser());

    @Override
    public void init() {
        Stream.of(BEAN_DEF_PARSERS).forEach(beanDefParser -> this.registerBeanDefinitionParser(beanDefParser.getElementName(), beanDefParser));
    }
}
