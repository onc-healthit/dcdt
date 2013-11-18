package gov.hhs.onc.dcdt.beans.factory.xml;


import org.springframework.beans.factory.xml.BeanDefinitionParser;

public interface ToolBeanDefinitionParser<T> extends BeanDefinitionParser {
    public String getElementName();
}
