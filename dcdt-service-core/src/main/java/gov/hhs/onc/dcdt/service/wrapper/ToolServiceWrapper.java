package gov.hhs.onc.dcdt.service.wrapper;

import gov.hhs.onc.dcdt.service.ToolService;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public interface ToolServiceWrapper<T extends ToolService> extends Runnable, WrapperEventListener {
    public void stop() throws Exception;

    public void start() throws Exception;
}
