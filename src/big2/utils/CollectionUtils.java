package big2.utils;

import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static <T> String setOfSetsToString(Set<Set<T>> set) {
        return String.format("[ %s ]",
                set.stream().map(CollectionUtils::setToString).collect(Collectors.joining(", ")));
    }

    public static <T> String setToString(Set<T> set) {
        return String.format("[ %s ]",
                set.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
