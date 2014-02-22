package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.AutoStartup;
import gov.hhs.onc.dcdt.beans.Phase;
import gov.hhs.onc.dcdt.beans.ToolLifecycleBean;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.task.AsyncListenableTaskExecutor;

public abstract class AbstractToolLifecycleBean extends AbstractToolBean implements ToolLifecycleBean {
    protected AsyncListenableTaskExecutor taskExec;
    protected boolean autoStartup;
    protected int phase = Phase.PHASE_PRECEDENCE_LOWEST;

    @Override
    public synchronized void stop(Runnable stopCallback) {
        if (this.isRunning()) {
            this.stop();

            this.taskExec.execute(stopCallback);
        }
    }

    @Override
    public synchronized void stop() {
    }

    @Override
    public synchronized void start() {
    }

    @Override
    public boolean isAutoStartup() {
        return ObjectUtils.defaultIfNull(ToolAnnotationUtils.getValue(AutoStartup.class, Boolean.class, this.getClass()), this.autoStartup);
    }

    @Override
    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    public int getPhase() {
        return ObjectUtils.defaultIfNull(ToolAnnotationUtils.getValue(Phase.class, Integer.class, this.getClass()), this.phase);
    }

    @Override
    public void setPhase(int phase) {
        this.phase = phase;
    }

    protected void setTaskExecutor(AsyncListenableTaskExecutor taskExec) {
        this.taskExec = taskExec;
    }
}
