package gov.hhs.onc.dcdt.beans;


import org.springframework.beans.factory.FactoryBean;

public interface ToolBeanFactoryBean<T extends ToolBean> extends FactoryBean<T> {
}
