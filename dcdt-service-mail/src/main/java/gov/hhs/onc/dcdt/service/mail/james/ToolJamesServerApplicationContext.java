package gov.hhs.onc.dcdt.service.mail.james;


import java.util.List;
import org.apache.james.container.spring.resource.JamesResourceLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class ToolJamesServerApplicationContext extends ClassPathXmlApplicationContext implements JamesResourceLoader {
    private JamesResourceLoader jamesResLoader = new ToolJamesResourceLoader();

    public ToolJamesServerApplicationContext(List<String> contextConfigLocs, ApplicationContext parentContext) {
        super(contextConfigLocs.toArray(new String[contextConfigLocs.size()]), false, parentContext);
    }

    @Override
    public Resource getResource(String loc) {
        Resource resource = null;

        try {
            resource = this.jamesResLoader.getResource(loc);
        } catch (Throwable th) {
        }

        return (resource != null) ? resource : super.getResource(loc);
    }

    @Override
    public String getAbsoluteDirectory() {
        return this.jamesResLoader.getAbsoluteDirectory();
    }

    @Override
    public String getConfDirectory() {
        return this.jamesResLoader.getConfDirectory();
    }

    @Override
    public String getVarDirectory() {
        return this.jamesResLoader.getVarDirectory();
    }

    @Override
    public String getRootDirectory() {
        return this.jamesResLoader.getRootDirectory();
    }
}
