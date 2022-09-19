package gg.souppvp.kitpvp;

import games.negative.framework.BasePlugin;
import gg.souppvp.kitpvp.api.KitPvPAPI;
import gg.souppvp.kitpvp.api.KitPvPDataManager;
import gg.souppvp.kitpvp.core.provider.KitPvPAPIProvider;

public final class KitPvP extends BasePlugin {

    @Override
    public void onEnable() {
        super.onEnable();
        // Plugin startup logic
        new KitPvPAPIProvider(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        KitPvPAPI api = KitPvPAPI.getInstance();
        KitPvPDataManager data = api.getDataManager();
        data.saveAll();
        data.closeConnection();
    }
}
