package gov.hhs.onc.dcdt.nio.channels;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContextAware;

public interface ChannelListener<T extends SelectableChannel & NetworkChannel, U extends SelectableChannel & ByteChannel & NetworkChannel, V extends SelectableChannel & ByteChannel & NetworkChannel, W extends SelectionAttachment, X extends ChannelListenerDataProcessor, Y extends ChannelListenerDataProcessorCallback<X, W>>
    extends ApplicationContextAware, ToolBean {
    @Nullable
    public SelectionOperationType processSelection(SelectionKey selKey) throws IOException;

    public void register(Selector selector) throws IOException;
}
