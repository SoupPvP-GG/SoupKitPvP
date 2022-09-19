package gg.souppvp.kitpvp.api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import gg.souppvp.kitpvp.api.model.Profile;
import gg.souppvp.kitpvp.api.model.response.ProfileCreationResponse;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface KitPvPDataManager {

    @Nullable
    Profile getProfile(@NotNull UUID uuid);

    @NotNull
    ProfileCreationResponse createProfile(@NotNull UUID uuid);

    @NotNull
    Collection<Profile> getProfiles();

    CompletableFuture<Void> deleteProfile(@NotNull Profile profile);

    CompletableFuture<Void> saveProfile(@NotNull Profile profile);

    MongoClient getClient();

    MongoDatabase getDatabase();

    MongoCollection<Document> getCollection();

    void saveAll();

    void closeConnection();
}
