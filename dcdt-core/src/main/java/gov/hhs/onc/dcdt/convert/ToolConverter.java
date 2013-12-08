package gov.hhs.onc.dcdt.convert;

import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

public interface ToolConverter extends ConditionalGenericConverter, ToolBean {
}
