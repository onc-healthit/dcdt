package gov.hhs.onc.dcdt.beans;

import org.springframework.core.PriorityOrdered;

public interface OverrideablePriorityOrdered extends PriorityOrdered {
    public void setOrder(int order);
}
