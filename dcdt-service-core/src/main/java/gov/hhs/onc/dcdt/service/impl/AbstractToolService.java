package gov.hhs.onc.dcdt.service.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolLifecycleBean;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.concurrent.impl.AbstractToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.impl.AbstractToolListenableFutureTask;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.service.ServiceContextConfiguration;
import gov.hhs.onc.dcdt.service.ToolService;
import gov.hhs.onc.dcdt.service.config.ToolServerConfig;
import gov.hhs.onc.dcdt.service.server.ToolServer;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.support.AbstractApplicationContext;

@ServiceContextConfiguration({ "spring/spring-core.xml", "spring/spring-core*.xml", "spring/spring-service.xml", "spring/spring-service-embedded.xml",
    "spring/spring-service-standalone.xml" })
public abstract class AbstractToolService<T extends ToolServerConfig, U extends ToolServer<T>> extends AbstractToolLifecycleBean implements ToolService<T, U> {
    protected static class ToolServerLifecycleTask extends AbstractToolListenableFutureTask<Void> {
        public ToolServerLifecycleTask(Callable<Void> callable) {
            super(callable);
        }
    }

    protected static class ToolServerLifecycleCallback extends AbstractToolListenableFutureCallback<Void, ToolServerLifecycleTask> {
        private CountDownLatch latch;

        public ToolServerLifecycleCallback(@Nullable ToolServerLifecycleTask task, CountDownLatch latch) {
            super(task);

            this.latch = latch;
        }

        @Override
        protected void onPostDone(@Nullable Void result, @Nullable Throwable exception) {
            this.latch.countDown();
        }
    }

    protected Class<T> serverConfigClass;
    protected Class<U> serverClass;
    protected AbstractApplicationContext appContext;
    protected List<U> servers;

    protected AbstractToolService(Class<T> serverConfigClass, Class<U> serverClass) {
        this.serverConfigClass = serverConfigClass;
        this.serverClass = serverClass;
    }

    @Override
    protected void stopInternal() throws Exception {
        this.executeServerLifecycle(Lifecycle::stop);
    }

    @Override
    protected void startInternal() throws Exception {
        this.executeServerLifecycle(Lifecycle::start);
    }

    protected void executeServerLifecycle(Consumer<U> method) throws Exception {
        CountDownLatch latch = new CountDownLatch(this.servers.size());
        ToolServerLifecycleCallback[] callbacks = this.servers.stream().map(server -> {
            ToolServerLifecycleTask task = new ToolServerLifecycleTask(() -> {
                method.accept(server);

                return null;
            });

            ToolServerLifecycleCallback callback = new ToolServerLifecycleCallback(task, latch);
            task.addCallback(callback);

            this.taskExec.submit(task);

            return callback;
        }).toArray(ToolServerLifecycleCallback[]::new);

        try {
            latch.await();
        } catch (InterruptedException ignored) {
            // noinspection ConstantConditions
            Stream.of(callbacks).forEach(callback -> callback.getTask().cancel(true));
        }

        for (ToolServerLifecycleCallback callback : callbacks) {
            if (callback.hasException()) {
                throw callback.getException();
            }
        }
    }

    @Override
    public boolean canStop() {
        return (super.canStop() && this.hasServers());
    }

    @Override
    public boolean canStart() {
        // noinspection ConstantConditions
        return (super.canStart() && this.hasServers() && ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class).isConfigured());
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    @Override
    public boolean hasServers() {
        return !CollectionUtils.isEmpty(this.servers);
    }

    @Override
    public List<U> getServers() {
        return this.servers;
    }

    @Override
    public void setServers(List<U> servers) {
        this.servers = servers;
    }
}
