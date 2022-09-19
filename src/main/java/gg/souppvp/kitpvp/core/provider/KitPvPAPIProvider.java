package gg.souppvp.kitpvp.core.provider;

import gg.souppvp.kitpvp.KitPvP;
import gg.souppvp.kitpvp.api.KitPvPAPI;
import gg.souppvp.kitpvp.api.KitPvPDataManager;

public class KitPvPAPIProvider extends KitPvPAPI {

    private final KitPvPDataManager dataManager;
    public KitPvPAPIProvider(KitPvP plugin) {
        setInstance(this);

        this.dataManager = new KitPvPDataManagerProvider(plugin);
    }

    @Override
    public KitPvPDataManager getDataManager() {
        return dataManager;
    }
}
