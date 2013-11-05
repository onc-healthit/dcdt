package gov.hhs.onc.dcdt.test;


import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextHierarchy({ @ContextConfiguration({ "classpath*:META-INF/spring/spring-core*.xml", "classpath*:spring/spring-core*.xml" }) })
public abstract class ToolTestNgTests extends AbstractTestNGSpringContextTests {
    protected AutowireCapableBeanFactory getBeanFactory() {
        return this.getApplicationContext().getAutowireCapableBeanFactory();
    }

    protected AbstractApplicationContext getApplicationContext() {
        return (AbstractApplicationContext) this.applicationContext;
    }
}
