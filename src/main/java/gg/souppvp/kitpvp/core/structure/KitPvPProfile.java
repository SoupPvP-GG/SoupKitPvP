package gg.souppvp.kitpvp.core.structure;

import gg.souppvp.kitpvp.api.model.Profile;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class KitPvPProfile implements Profile {

    private final UUID uniqueID;
    private int kills;
    private int deaths;
    private int killStreak;
    private int bestKillStreak;
    private long coins;
    private String currentKit;

    public KitPvPProfile(Document document) {
        this.uniqueID = UUID.fromString(document.getString("uuid"));
        this.kills = document.getInteger("kills");
        this.deaths = document.getInteger("deaths");
        this.killStreak = document.getInteger("killstreak");
        this.bestKillStreak = document.getInteger("best-killstreak");
        this.coins = document.getLong("coins");
        this.currentKit = document.getString("current-kit");
    }

    public KitPvPProfile(UUID uuid) {
        this.uniqueID = uuid;
    }

    @Override
    public @NotNull UUID getUniqueID() {
        return uniqueID;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int amount) {
        this.kills = amount;
    }

    @Override
    public int getDeaths() {
        return deaths;
    }

    @Override
    public void setDeaths(int amount) {
        this.deaths = amount;
    }

    @Override
    public int getKillStreak() {
        return killStreak;
    }

    @Override
    public void setKillStreak(int amount) {
        this.killStreak = amount;
    }

    @Override
    public int getBestKillStreak() {
        return bestKillStreak;
    }

    @Override
    public void setBestKillStreak(int amount) {
        this.bestKillStreak = amount;
    }

    @Override
    public long getCoins() {
        return coins;
    }

    @Override
    public void setCoins(long amount) {
        this.coins = amount;
    }

    @Override
    public @Nullable String getCurrentKit() {
        return currentKit;
    }

    @Override
    public void setCurrentKit(@Nullable String kit) {
        this.currentKit = kit;
    }

    @Override
    public @NotNull Document toDocument() {
        return new Document()
                .append("uuid", uniqueID.toString())
                .append("kills", kills)
                .append("deaths", deaths)
                .append("killstreak", killStreak)
                .append("best-killstreak", bestKillStreak)
                .append("coins", coins)
                .append("current-kit", currentKit);
    }
}
