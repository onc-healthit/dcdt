package gov.hhs.onc.dcdt.format.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.format.ToolFormatter;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.text.ParseException;
import java.util.Locale;
import javax.annotation.Nullable;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class AbstractToolFormatter<T> extends AbstractToolBean implements ToolFormatter<T> {
    protected Class<T> formattedClass;
    protected boolean parse;
    protected boolean print;

    protected AbstractToolFormatter(Class<T> formattedClass) {
        this(formattedClass, true, true);
    }

    protected AbstractToolFormatter(Class<T> formattedClass, boolean parse, boolean print) {
        this.formattedClass = formattedClass;
        this.parse = parse;
        this.print = print;
    }

    @Nullable
    @Override
    public String print(@Nullable T obj) {
        return this.print(obj, LocaleContextHolder.getLocale());
    }

    @Nullable
    @Override
    public String print(@Nullable T obj, Locale locale) {
        try {
            return ((obj != null) ? this.printInternal(obj, locale) : null);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                "Unable to print (class=%s) object (class=%s, formattedClass=%s) as localized string (locale={%s}).", ToolClassUtils.getName(this),
                ToolClassUtils.getName(obj), ToolClassUtils.getName(this.formattedClass), locale), e);
        }
    }

    @Nullable
    @Override
    public T parse(@Nullable String str) throws ParseException {
        return this.parse(str, LocaleContextHolder.getLocale());
    }

    @Nullable
    @Override
    public T parse(@Nullable String str, Locale locale) throws ParseException {
        try {
            return ((str != null) ? this.parseInternal(str, locale) : null);
        } catch (IllegalArgumentException | ParseException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Unable to parse (class=%s) localized string (locale={%s}) into object (class=%s): %s",
                ToolClassUtils.getName(this), locale, ToolClassUtils.getName(this.formattedClass), str), e);
        }
    }

    @Nullable
    protected String printInternal(T obj, Locale locale) throws Exception {
        return obj.toString();
    }

    @Nullable
    protected T parseInternal(String str, Locale locale) throws Exception {
        return null;
    }

    @Override
    public Class<T> getFormattedClass() {
        return this.formattedClass;
    }

    @Override
    public boolean canParse() {
        return this.parse;
    }

    @Override
    public boolean canPrint() {
        return this.print;
    }
}
