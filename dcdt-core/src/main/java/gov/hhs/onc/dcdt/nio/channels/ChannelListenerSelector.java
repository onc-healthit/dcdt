package gov.hhs.onc.dcdt.nio.channels;

import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import java.util.List;
import javax.annotation.Nullable;

public interface ChannelListenerSelector extends ToolLifecycleBean {
    public List<ChannelListener<?, ?, ?, ?, ?, ?>> getChannelListeners();

    public void setChannelListeners(@Nullable ChannelListener<?, ?, ?, ?, ?, ?> ... channelListeners);

    public void setChannelListeners(@Nullable Iterable<ChannelListener<?, ?, ?, ?, ?, ?>> channelListeners);
}
