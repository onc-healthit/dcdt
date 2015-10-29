package gov.hhs.onc.dcdt.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections4.list.SetUniqueList;

public final class ToolComparatorUtils {
    public static class OrderedComparator<T> implements Comparator<T> {
        private SetUniqueList<T> orderObjs;

        public OrderedComparator(List<T> orderObjs) {
            this.orderObjs = SetUniqueList.setUniqueList(orderObjs);
        }

        @Override
        public int compare(T obj1, T obj2) {
            int objIndex1 = this.orderObjs.indexOf(obj1), objIndex2 = this.orderObjs.indexOf(obj2);

            if (objIndex1 != -1) {
                return ((objIndex2 != -1) ? Integer.compare(objIndex1, objIndex2) : -1);
            } else if (objIndex2 != -1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private ToolComparatorUtils() {
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T> Comparator<T> comparingOrder(T ... orderObjs) {
        return comparingOrder(ToolArrayUtils.asList(orderObjs));
    }

    public static <T> Comparator<T> comparingOrder(List<T> orderObjs) {
        return new OrderedComparator<>(orderObjs);
    }
}
