package gov.hhs.onc.dcdt.format.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.convert.utils.ToolConversionUtils;
import gov.hhs.onc.dcdt.convert.utils.ToolConversionUtils.IsAssignableConvertiblePredicate;
import gov.hhs.onc.dcdt.format.ToolFormatter;
import gov.hhs.onc.dcdt.format.ToolFormatterRegistrar;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.format.FormatterRegistry;

public class ToolFormatterRegistrarImpl extends AbstractToolBean implements ToolFormatterRegistrar {
    private static class ToolFormatterConverter<T> implements ConditionalGenericConverter {
        protected ToolFormatter<T> formatter;
        protected Class<T> formattedClass;
        protected ConvertiblePair parseConvType;
        protected ConvertiblePair printConvType;
        protected Set<ConvertiblePair> convTypes = new HashSet<>(2);

        public ToolFormatterConverter(ToolFormatter<T> formatter) {
            this.formatter = formatter;
            this.formattedClass = this.formatter.getFormattedClass();
            this.parseConvType = new ConvertiblePair(String.class, this.formattedClass);
            this.printConvType = new ConvertiblePair(this.formattedClass, String.class);

            if (this.formatter.canParse()) {
                this.convTypes.add(this.parseConvType);
            }

            if (this.formatter.canPrint()) {
                this.convTypes.add(this.printConvType);
            }
        }

        @Nullable
        @Override
        public Object convert(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
            Object target = null;

            if (ToolConversionUtils.isAssignable(srcType, targetType, this.parseConvType)) {
                try {
                    target = this.formatter.parse(((String) src));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(String.format("Unable to convert parseable source string to target object (class=%s): %s",
                        ToolClassUtils.getName(this.formattedClass), src), e);
                }
            } else if (ToolConversionUtils.isAssignable(srcType, targetType, this.printConvType)) {
                target = this.formatter.print(this.formattedClass.cast(src));
            }

            return target;
        }

        @Override
        public boolean matches(TypeDescriptor srcType, TypeDescriptor targetType) {
            return CollectionUtils.exists(this.convTypes, new IsAssignableConvertiblePredicate(srcType, targetType));
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return this.convTypes;
        }
    }

    private Set<ToolFormatter<?>> formatters;

    public ToolFormatterRegistrarImpl(Set<ToolFormatter<?>> formatters) {
        this.formatters = formatters;
    }

    @Override
    public void registerFormatters(FormatterRegistry formatterReg) {
        for (ToolFormatter<?> formatter : this.formatters) {
            formatterReg.addConverter(new ToolFormatterConverter<>(formatter));
        }
    }

    @Override
    public Set<ToolFormatter<?>> getFormatters() {
        return this.formatters;
    }
}
