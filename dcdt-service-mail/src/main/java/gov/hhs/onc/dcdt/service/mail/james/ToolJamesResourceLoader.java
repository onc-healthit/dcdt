package gov.hhs.onc.dcdt.service.mail.james;


import gov.hhs.onc.dcdt.utils.ToolFileUtils;
import java.nio.file.Paths;
import org.apache.commons.lang3.SystemUtils;
import org.apache.james.container.spring.resource.AbstractJamesResourceLoader;

public class ToolJamesResourceLoader extends AbstractJamesResourceLoader {
    private final static String ROOT_DIR = Paths.get(SystemUtils.getUserDir().getAbsolutePath()).toString();
    private final static String CONF_DIR = Paths.get(ROOT_DIR, "conf", "mail", "james").toString();
    private final static String VAR_DIR = Paths.get(ROOT_DIR, "var", "james").toString();

    @Override
    public String getAbsoluteDirectory() {
        return ToolFileUtils.ABS_PATH_PREFIX;
    }

    @Override
    public String getConfDirectory() {
        return CONF_DIR;
    }

    @Override
    public String getVarDirectory() {
        return VAR_DIR;
    }

    @Override
    public String getRootDirectory() {
        return ROOT_DIR;
    }
}
