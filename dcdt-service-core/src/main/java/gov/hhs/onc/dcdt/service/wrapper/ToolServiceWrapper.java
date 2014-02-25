package gov.hhs.onc.dcdt.service.wrapper;

import gov.hhs.onc.dcdt.service.ToolService;
import org.tanukisoftware.wrapper.event.WrapperEventListener;

public interface ToolServiceWrapper<T extends ToolService> extends WrapperEventListener {
    public void stop();

    public void start();
}
