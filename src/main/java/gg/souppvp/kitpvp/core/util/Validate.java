package gg.souppvp.kitpvp.core.util;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Validate {

    public void notNull(@Nullable Object object, @NotNull String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public void notNull(@Nullable Object object) {
        notNull(object, "Object cannot be null");
    }
}
