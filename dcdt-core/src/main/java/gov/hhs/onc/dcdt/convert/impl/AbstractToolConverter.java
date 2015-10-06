package gov.hhs.onc.dcdt.convert.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.convert.utils.ToolConversionUtils.IsAssignableConvertiblePredicate;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

public abstract class AbstractToolConverter extends AbstractToolBean implements ToolConverter {
    protected static class ConvertAnnotationTransformer extends AbstractToolTransformer<Convert, ConvertiblePair> {
        public final static ConvertAnnotationTransformer INSTANCE = new ConvertAnnotationTransformer();

        @Override
        protected ConvertiblePair transformInternal(Convert convAnno) throws Exception {
            return new ConvertiblePair(convAnno.from(), convAnno.to());
        }
    }

    protected final static TypeDescriptor TYPE_DESC_BYTE = TypeDescriptor.valueOf(byte.class);
    protected final static TypeDescriptor TYPE_DESC_BYTE_ARR = TypeDescriptor.array(TYPE_DESC_BYTE);
    protected final static TypeDescriptor TYPE_DESC_STR = TypeDescriptor.valueOf(String.class);
    protected final static TypeDescriptor TYPE_DESC_STR_ARR = TypeDescriptor.array(TYPE_DESC_STR);

    protected Set<ConvertiblePair> convTypes = new HashSet<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractToolConverter.class);

    @Nullable
    @Override
    public <T, U> U convert(@Nullable T src, Class<T> srcClass, Class<U> targetClass) {
        return targetClass.cast(this.convert(src, TypeDescriptor.valueOf(srcClass), TypeDescriptor.valueOf(targetClass)));
    }

    @Nullable
    @Override
    public Object convert(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        ConvertiblePair convType = this.findConvertibleType(src, srcType, targetType);

        if (!this.canConvert(src, srcType, targetType, convType)) {
            return null;
        }

        try {
            Object target = this.convertInternal(src, srcType, targetType, convType);

            LOGGER.trace(String.format("Converted (class=%s) source object (class=%s, srcClass=%s) to target object (class=%s, targetClass=%s).",
                ToolClassUtils.getName(this), ToolClassUtils.getName(src), ToolClassUtils.getName(srcType.getType()), ToolClassUtils.getName(target),
                ToolClassUtils.getName(targetType.getType())));

            return target;
        } catch (ConversionFailedException e) {
            throw e;
        } catch (Exception e) {
            throw new ConversionFailedException(srcType, targetType, src, e);
        }
    }

    @Override
    public boolean canConvert(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType, @Nullable ConvertiblePair convType) {
        return ((src != null) && (convType != null));
    }

    @Nullable
    @Override
    public ConvertiblePair findConvertibleType(@Nullable Object src, TypeDescriptor srcType, TypeDescriptor targetType) {
        return CollectionUtils.find(this.convTypes, new IsAssignableConvertiblePredicate(srcType, targetType));
    }

    @Override
    public boolean matches(TypeDescriptor srcType, TypeDescriptor targetType) {
        return CollectionUtils.exists(this.convTypes, new IsAssignableConvertiblePredicate(srcType, targetType));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Class<? extends ToolConverter> convClass = this.getClass();

        ToolCollectionUtils.addAll(this.convTypes, ((Iterable<? extends ConvertiblePair>) CollectionUtils.collect(
            ToolCollectionUtils.addAll(ToolAnnotationUtils.findAnnotations(Convert.class, convClass),
                IteratorUtils.asIterable(ToolIteratorUtils.chainedArrayIterator(ToolAnnotationUtils.getValues(Converts.class, Convert[].class, convClass)))),
            ConvertAnnotationTransformer.INSTANCE)));
    }

    @Nullable
    protected abstract Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convType) throws Exception;

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.convTypes;
    }
}
