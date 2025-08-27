package dev.enco.greatcombat.utils;

import lombok.experimental.UtilityClass;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class for working with Enum types and converting between strings and EnumSets.
 */
@UtilityClass
public class EnumUtils {
    /**
     * Converts a list of string values to an EnumSet of the specified enum class.
     * Handles invalid enum values by calling the provided error consumer.
     *
     * @param <T> The enum type
     * @param values List of string values to convert to enum constants
     * @param enumClass The class of the enum to convert to
     * @param onError Consumer to handle invalid enum values
     * @return EnumSet containing the successfully converted enum constants
     */
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
