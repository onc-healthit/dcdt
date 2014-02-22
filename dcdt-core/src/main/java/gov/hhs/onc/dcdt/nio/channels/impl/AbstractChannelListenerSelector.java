package gov.hhs.onc.dcdt.nio.channels.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.nio.channels.ChannelListener;
import gov.hhs.onc.dcdt.nio.channels.ChannelListenerSelector;
import gov.hhs.onc.dcdt.nio.channels.SelectionOperationType;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractChannelListenerSelector extends AbstractToolLifecycleBean implements ChannelListenerSelector {
    protected class ChannelListenerSelectorDaemon implements Callable<Void> {
        @Nullable
        @Override
        public Void call() throws Exception {
            Iterator<SelectionKey> selKeysIterator;
            SelectionKey selKey;
            SelectionOperationType selOpType;

            while (AbstractChannelListenerSelector.this.isRunning()) {
                if (AbstractChannelListenerSelector.this.selector.select() == 0) {
                    continue;
                }

                while ((selKeysIterator = AbstractChannelListenerSelector.this.selector.selectedKeys().iterator()).hasNext()
                    && ((selKey = selKeysIterator.next()) != null)) {
                    selKeysIterator.remove();

                    if (!selKey.isValid()) {
                        continue;
                    }

                    selOpType = null;

                    for (ChannelListener<?, ?, ?, ?, ?, ?> channelListener : AbstractChannelListenerSelector.this.channelListeners) {
                        if ((selOpType = channelListener.processSelection(selKey)) != null) {
                            break;
                        }
                    }

                    if (selOpType == null) {
                        AbstractChannelListenerSelector.LOGGER.warn(String.format(
                            "Channel listener selector (class=%s) selection (readyOps=%d, channelClass=%s, attachmentClass=%s) not processed.",
                            ToolClassUtils.getName(AbstractChannelListenerSelector.this), selKey.readyOps(), ToolClassUtils.getName(selKey.channel()),
                            ToolClassUtils.getName(selKey.attachment())));

                        selKey.cancel();
                    }
                }
            }

            return null;
        }
    }

    protected List<ChannelListener<?, ?, ?, ?, ?, ?>> channelListeners = new ArrayList<>();
    protected Selector selector;
    protected FutureTask<Void> selectorDaemonTask;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractChannelListenerSelector.class);

    @Override
    public boolean isRunning() {
        return super.isRunning() && (this.selector != null) && this.selector.isOpen();
    }

    @Override
    protected synchronized void stopInternal() throws Exception {
        if ((this.selectorDaemonTask != null) && !this.selectorDaemonTask.isDone()) {
            this.selectorDaemonTask.cancel(true);
        }

        this.selector.close();
    }

    @Override
    protected synchronized void startInternal() throws Exception {
        this.selector = Selector.open();

        for (ChannelListener<?, ?, ?, ?, ?, ?> channelListener : this.channelListeners) {
            channelListener.register(this.selector);
        }

        this.taskExec.execute((this.selectorDaemonTask = new FutureTask<>(new ChannelListenerSelectorDaemon())));
    }

    @Override
    public List<ChannelListener<?, ?, ?, ?, ?, ?>> getChannelListeners() {
        return this.channelListeners;
    }

    @Override
    public void setChannelListeners(@Nullable ChannelListener<?, ?, ?, ?, ?, ?> ... channelListeners) {
        this.setChannelListeners(ToolArrayUtils.asList(channelListeners));
    }

    @Override
    public void setChannelListeners(@Nullable Iterable<ChannelListener<?, ?, ?, ?, ?, ?>> channelListeners) {
        this.channelListeners.clear();
        ToolCollectionUtils.addAll(this.channelListeners, channelListeners);
    }
}
