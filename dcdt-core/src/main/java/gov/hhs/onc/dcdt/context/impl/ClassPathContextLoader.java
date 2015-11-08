package gov.hhs.onc.dcdt.context.impl;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClassPathContextLoader extends AbstractToolContextLoader<ClassPathXmlApplicationContext> {
    @Override
    protected void buildContext(String ... configLocs) throws Exception {
        this.appContext = new ClassPathXmlApplicationContext(configLocs, false);
    }
}
