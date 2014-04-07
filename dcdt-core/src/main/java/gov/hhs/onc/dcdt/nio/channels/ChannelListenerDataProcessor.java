package gov.hhs.onc.dcdt.nio.channels;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.concurrent.Callable;

public interface ChannelListenerDataProcessor extends Callable<byte[]>, ToolBean {
}
