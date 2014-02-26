package gov.hhs.onc.dcdt.web.controller;

import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSourceAware;

public interface ToolController extends ApplicationContextAware, MessageSourceAware, ToolBean {
}
