package gov.hhs.onc.dcdt.velocity.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolPropertyUtils;
import gov.hhs.onc.dcdt.velocity.utils.ToolVelocityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

public class ToolVelocityEngineFactoryBean extends VelocityEngineFactoryBean {
    private Charset encoding;
    private String[] vmLibs;

    @Override
    public void afterPropertiesSet() throws IOException, VelocityException {
        Map<String, Object> velocityPropsMap = new HashMap<>();

        if (this.encoding != null) {
            String encodingName = this.encoding.name();

            velocityPropsMap.put(RuntimeConstants.INPUT_ENCODING, encodingName);
            velocityPropsMap.put(RuntimeConstants.OUTPUT_ENCODING, encodingName);
        }

        if (!ArrayUtils.isEmpty(this.vmLibs)) {
            String[] vmLibPaths = ArrayUtils.clone(this.vmLibs);

            IntStream.range(0, vmLibPaths.length).forEach(a -> vmLibPaths[a] = StringUtils.appendIfMissing(vmLibPaths[a], ToolVelocityUtils.FILE_EXT_VM));

            velocityPropsMap.put(RuntimeConstants.VM_LIBRARY, ToolPropertyUtils.joinValues(vmLibPaths));
        }

        this.setVelocityPropertiesMap(velocityPropsMap);

        super.afterPropertiesSet();
    }

    @Override
    protected void initSpringResourceLoader(VelocityEngine velocityEngine, String resourceLoaderPath) {
        super.initSpringResourceLoader(velocityEngine, resourceLoaderPath);

        velocityEngine.setProperty(ToolSpringResourceLoader.SPRING_RESOURCE_LOADER_CLASS, ToolClassUtils.getName(ToolSpringResourceLoader.class));
    }

    public Charset getEncoding() {
        return this.encoding;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public String[] getVelocimacroLibraries() {
        return this.vmLibs;
    }

    public void setVelocimacroLibraries(String[] vmLibs) {
        this.vmLibs = vmLibs;
    }
}
