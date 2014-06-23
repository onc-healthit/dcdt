package gov.hhs.onc.dcdt.format;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.text.ParseException;
import javax.annotation.Nullable;
import org.springframework.format.Formatter;

public interface ToolFormatter<T> extends Formatter<T>, ToolBean {
    @Nullable
    public String print(@Nullable T obj);

    @Nullable
    public T parse(@Nullable String str) throws ParseException;

    public Class<T> getFormattedClass();

    public boolean canParse();

    public boolean canPrint();
}
