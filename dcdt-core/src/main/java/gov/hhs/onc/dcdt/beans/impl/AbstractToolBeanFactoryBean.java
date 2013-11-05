package gov.hhs.onc.dcdt.beans.impl;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.ToolBeanFactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractToolBeanFactoryBean<T extends ToolBean> extends AbstractFactoryBean<T> implements ToolBeanFactoryBean<T> {
}
