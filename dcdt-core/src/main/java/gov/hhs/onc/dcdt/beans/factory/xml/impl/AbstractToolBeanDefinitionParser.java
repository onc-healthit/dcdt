package gov.hhs.onc.dcdt.beans.factory.xml.impl;


import gov.hhs.onc.dcdt.beans.factory.xml.ToolBeanDefinitionParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractToolBeanDefinitionParser<T> extends AbstractSimpleBeanDefinitionParser implements ToolBeanDefinitionParser<T> {
    protected final static String ATTR_NAME_SCOPE = "scope";

    protected Class<T> beanClass;
    protected String elemName;

    protected AbstractToolBeanDefinitionParser(Class<T> beanClass, String elemName) {
        this.beanClass = beanClass;
        this.elemName = elemName;
    }

    @Override
    protected void doParse(Element elem, ParserContext parserContext, BeanDefinitionBuilder beanDefBuilder) {
        super.doParse(elem, parserContext, beanDefBuilder);

        String scope = elem.getAttribute(ATTR_NAME_SCOPE);

        if (!StringUtils.isEmpty(scope)) {
            beanDefBuilder.setScope(scope);
        }
    }

    @Override
    protected boolean isEligibleAttribute(String attrName) {
        return super.isEligibleAttribute(attrName) && !StringUtils.equals(attrName, ATTR_NAME_SCOPE);
    }

    @Override
    protected Class<?> getBeanClass(Element elem) {
        return this.beanClass;
    }

    @Override
    public String getElementName() {
        return this.elemName;
    }
}
