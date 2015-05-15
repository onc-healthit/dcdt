package gov.hhs.onc.dcdt.data.utils;

import javax.annotation.Nullable;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

public abstract class ToolRestrictionsUtils {
    public static DetachedCriteria addAll(DetachedCriteria detachedCriteria, @Nullable Iterable<Criterion> criterions) {
        if (criterions != null) {
            for (Criterion criterion : criterions) {
                detachedCriteria.add(criterion);
            }
        }

        return detachedCriteria;
    }
}
