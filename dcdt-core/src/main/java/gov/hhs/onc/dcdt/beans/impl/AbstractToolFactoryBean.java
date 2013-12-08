package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.factory.ToolFactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public abstract class AbstractToolFactoryBean<T> extends AbstractFactoryBean<T> implements ToolFactoryBean<T> {
}
