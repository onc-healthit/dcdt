package gov.hhs.onc.dcdt.config;


import gov.hhs.onc.dcdt.test.ToolTestNgUnitTests;
import org.springframework.beans.factory.config.BeanDefinition;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.all" }, groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.config.all",
    "dcdt.test.unit.config.instance" })
public class InstanceConfigUnitTests extends ToolTestNgUnitTests {
    private final static String INSTANCE_CONFIG_BEAN_NAME = "instanceConfigTest";
    
    @Test
    public void testBeanDefinition() {
        Assert.assertTrue(this.beanDefReg.containsBeanDefinition(INSTANCE_CONFIG_BEAN_NAME), "Unable to find instance configuration Spring bean (name="
            + INSTANCE_CONFIG_BEAN_NAME + ").");

        BeanDefinition instanceBeanDef = this.beanDefReg.getBeanDefinition(INSTANCE_CONFIG_BEAN_NAME);

        this.beanDefReg.removeBeanDefinition(INSTANCE_CONFIG_BEAN_NAME);
        this.getApplicationContext().refresh();

        Assert.assertFalse(this.beanDefReg.containsBeanDefinition(INSTANCE_CONFIG_BEAN_NAME), "Unable to remove instance configuration Spring bean (name="
            + INSTANCE_CONFIG_BEAN_NAME + ").");

        this.beanDefReg.registerBeanDefinition(INSTANCE_CONFIG_BEAN_NAME, instanceBeanDef);
        this.getApplicationContext().refresh();

        Assert.assertTrue(this.beanDefReg.containsBeanDefinition(INSTANCE_CONFIG_BEAN_NAME), "Unable to re-register instance configuration Spring bean (name="
            + INSTANCE_CONFIG_BEAN_NAME + ").");
    }
}
