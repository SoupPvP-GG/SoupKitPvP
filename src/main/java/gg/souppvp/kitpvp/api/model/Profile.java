package gg.souppvp.kitpvp.api.model;

import gg.souppvp.kitpvp.core.structure.KitPvPProfile;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Profile {

    /**
     * Gets the unique id of the {@link Profile}
     * @return {@link UUID} instance
     */
    @NotNull
    UUID getUniqueID();

    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getUniqueID());
    }

    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueID());
    }

    int getKills();

    void setKills(int amount);

    default void addKills(int amount) {
        setKills(getKills() + amount);
    }

    int getDeaths();

    void setDeaths(int amount);

    default void addDeaths(int amount) {
        setDeaths(getDeaths() + amount);
    }

    int getKillStreak();

    void setKillStreak(int amount);

    // returning int, so we could announce killstreaks
    // once it hits a certain threshold
    default int addKillStreak(int amount) {
        setKillStreak(getKillStreak() + amount);
        return getKillStreak();
    }

    int getBestKillStreak();

    void setBestKillStreak(int amount);

    default void addBestKillStreak(int amount) {
        setBestKillStreak(getBestKillStreak() + amount);
    }

    long getCoins();

    void setCoins(long amount);

    default void addCoins(long amount) {
        setCoins(getCoins() + amount);
    }

    default void removeCoins(long amount) {
        setCoins(getCoins() - amount);
        if (getCoins() < 0) {
            setCoins(0);
        }
    }

    default boolean transactCoins(long amount) {
        if (getCoins() < amount) {
            return false;
        }
        removeCoins(amount);
        return true;
    }

    @Nullable
    String getCurrentKit();

    // Exists for the soul purpose of not needing to make a null-check later on
    @NotNull
    default String getSafeCurrentKit() {
        return getCurrentKit() == null ? "N/A" : getCurrentKit();
    }

    void setCurrentKit(@Nullable String kit);

    @NotNull
    Document toDocument();

    static Profile fromDocument(@NotNull Document document) {
        return new KitPvPProfile(document);
    }

}
