package gov.hhs.onc.dcdt.mail.sender;

import gov.hhs.onc.dcdt.beans.ToolBean;
import org.springframework.mail.javamail.MimeMessagePreparator;

public interface ToolMimeMessagePreparator extends MimeMessagePreparator, ToolBean {
    public void prepare() throws Exception;
}
