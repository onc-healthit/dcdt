package gov.hhs.onc.dcdt.test;

import gov.hhs.onc.dcdt.context.ToolContextLoader;
import org.springframework.test.context.ContextLoader;

public interface ToolTestContextLoader extends ContextLoader, ToolContextLoader {
}
