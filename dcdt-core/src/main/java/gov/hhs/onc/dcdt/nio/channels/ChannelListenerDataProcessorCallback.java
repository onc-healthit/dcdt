package gov.hhs.onc.dcdt.nio.channels;

import org.springframework.util.concurrent.ListenableFutureCallback;

public interface ChannelListenerDataProcessorCallback<T extends ChannelListenerDataProcessor, U extends SelectionAttachment> extends
    ListenableFutureCallback<byte[]> {
}
