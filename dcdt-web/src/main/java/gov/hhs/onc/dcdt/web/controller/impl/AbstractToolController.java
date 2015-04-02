package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.HttpHeaderNames;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.ToolController;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.MimeType;

public abstract class AbstractToolController extends AbstractToolBean implements ToolController, MessageSourceAware {
    @Resource(name = "conversionService")
    protected ConversionService convService;

    @Resource(name = "messageSourceValidation")
    protected MessageSource msgSourceValidation;

    protected MessageSource msgSource;

    protected AbstractApplicationContext appContext;

    protected static void buildFileResponse(HttpServletResponse servletResp, String fileName, MimeType fileContentType, byte[] fileData)
        throws ToolWebException {
        ServletOutputStream servletOutStream;

        try {
            servletResp.setHeader(HttpHeaderNames.HEADER_NAME_CONTENT_DISPOSITION, (HttpHeaderNames.HEADER_VALUE_CONTENT_DISPOSITION_ATTACHMENT
                + HttpHeaderNames.DELIM_HEADER_VALUE + HttpHeaderNames.HEADER_VALUE_PARAM_NAME_CONTENT_DISPOSITION_FILENAME
                + HttpHeaderNames.DELIM_HEADER_VALUE_PARAM + ToolStringUtils.quote(fileName)));
            servletResp.setContentType(fileContentType.toString());
            servletResp.setContentLength(fileData.length);

            IOUtils.write(fileData, (servletOutStream = servletResp.getOutputStream()));
            servletOutStream.flush();
        } catch (IOException e) {
            throw new ToolWebException(String.format("Unable to write file (name=%s, contentType={%s}) data (len=%d) to servlet output stream.", fileName,
                fileContentType, fileData.length), e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.msgSource = messageSource;
    }
}
