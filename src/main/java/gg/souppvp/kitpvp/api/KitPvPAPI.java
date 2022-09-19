package gg.souppvp.kitpvp.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class KitPvPAPI {

    @Getter @Setter(AccessLevel.PROTECTED)
    private static KitPvPAPI instance;

    public abstract KitPvPDataManager getDataManager();
}
