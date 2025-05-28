package dev.enco.greatcombat.utils;

import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

@UtilityClass
public class EnumUtils {
    public <T extends Enum<T>> EnumSet<T> toEnumSet(
            List<String> values,
            Class<T> enumClass,
            Consumer<String> onError
    ) {
        EnumSet<T> set = EnumSet.noneOf(enumClass);
        for (String value : values) {
            try {
                set.add(Enum.valueOf(enumClass, value));
            } catch (IllegalArgumentException e) {
                onError.accept(value);
            }
        }
        return set;
    }
}
