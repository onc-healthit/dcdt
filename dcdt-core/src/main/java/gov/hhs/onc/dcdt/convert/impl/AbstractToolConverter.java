package gov.hhs.onc.dcdt.convert.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.convert.ConversionRuntimeException;
import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.utils.ToolAnnotationUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;

public abstract class AbstractToolConverter extends AbstractToolBean implements ToolConverter {
    protected final static TypeDescriptor TYPE_DESC_STR = TypeDescriptor.valueOf(String.class);
    protected final static TypeDescriptor TYPE_DESC_STR_ARR = TypeDescriptor.array(TYPE_DESC_STR);

    protected Set<ConvertiblePair> convertPairs;

    @Nullable
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        ConvertiblePair convertPair = this.findConvertibleTypes(sourceType, targetType);

        try {
            return this.convertInternal(source, sourceType, targetType, convertPair);
        } catch (Exception e) {
            throw new ConversionRuntimeException(String.format("Unable to convert source (sourceClass=%s, class=%s) to target (targetClass=%s).",
                ToolClassUtils.getName(sourceType), ToolClassUtils.getName(source), ToolClassUtils.getName(targetType)), e);
        }
    }

    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return this.findConvertibleTypes(sourceType, targetType) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return this.convertPairs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initializeConvertibleTypesInternal();
    }

    @Nullable
    protected abstract Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair)
        throws Exception;

    @Nullable
    protected ConvertiblePair findConvertibleTypes(TypeDescriptor sourceType, TypeDescriptor targetType) {
        for (ConvertiblePair convertPair : this.convertPairs) {
            if (sourceType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getSourceType()))
                && targetType.isAssignableTo(TypeDescriptor.valueOf(convertPair.getTargetType()))) {
                return convertPair;
            }
        }

        return null;
    }

    protected void initializeConvertibleTypesInternal() {
        List<Converts> convertsAnnos = ToolArrayUtils.unwrapElements(ToolAnnotationUtils.getValues(Converts.List.class, Converts[].class, this.getClass()));
        ToolCollectionUtils.addAll(convertsAnnos, ToolAnnotationUtils.findAnnotations(Converts.class, this.getClass()));

        this.convertPairs = new LinkedHashSet<>(convertsAnnos.size());

        for (Converts convertsAnno : convertsAnnos) {
            convertPairs.add(new ConvertiblePair(convertsAnno.from(), convertsAnno.to()));
        }
    }
}
