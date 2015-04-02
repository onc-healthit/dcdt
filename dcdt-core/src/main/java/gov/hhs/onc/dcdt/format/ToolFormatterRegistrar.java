package gov.hhs.onc.dcdt.format;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.Set;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistrar;

public interface ToolFormatterRegistrar extends FormatterRegistrar, ToolBean {
    public Set<? extends Formatter<?>> getFormatters();
}
