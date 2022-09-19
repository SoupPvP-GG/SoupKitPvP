package gg.souppvp.kitpvp.core.provider;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import games.negative.framework.util.Utils;
import gg.souppvp.kitpvp.KitPvP;
import gg.souppvp.kitpvp.api.KitPvPDataManager;
import gg.souppvp.kitpvp.api.model.Profile;
import gg.souppvp.kitpvp.api.model.response.ProfileCreationResponse;
import gg.souppvp.kitpvp.config.MongoConfig;
import gg.souppvp.kitpvp.core.structure.KitPvPProfile;
import gg.souppvp.kitpvp.core.util.Validate;
import lombok.SneakyThrows;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class KitPvPDataManagerProvider implements KitPvPDataManager {

    private MongoConfig mongoConfig;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    private final Map<UUID, Profile> profiles = Maps.newHashMap();

    @SneakyThrows
    public KitPvPDataManagerProvider(KitPvP plugin) {
        File file = new File(plugin.getDataFolder() + "/configs", "mongo.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();

            this.mongoConfig = new MongoConfig();
            saveConfig(file);
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            this.mongoConfig = gson.fromJson(reader, MongoConfig.class);
            saveConfig(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Validate all credentials are not null
        Validate.notNull(mongoConfig, "For some reason the configuration file for MongoDB is null.");
        Validate.notNull(mongoConfig.getUser(), "The `username` field in mongo.json is null.");
        Validate.notNull(mongoConfig.getPass(), "The `password` field in mongo.json is null.");
        Validate.notNull(mongoConfig.getDatabase(), "The `database` field in mongo.json is null.");
        Validate.notNull(mongoConfig.getPort(), "The `port` field in mongo.json is null.");
        Validate.notNull(mongoConfig.getCollection(), "The `collection` field in mongo.json is null.");

        // domain = data01.negative.games
        String uri = "mongodb://{user}:{pass}@{domain}:{port}/";
        uri = uri.replace("{user}", mongoConfig.getUser());
        uri = uri.replace("{pass}", mongoConfig.getPass());
        uri = uri.replace("{domain}", mongoConfig.getDomain());
        uri = uri.replace("{port}", mongoConfig.getPort());

        try (MongoClient client = MongoClients.create(uri)) {
            this.client = client;
            this.database = client.getDatabase(mongoConfig.getDatabase());
        } catch (MongoException e) {
            e.printStackTrace();
            return;
        }

        // TODO: 2022-09-19 Something is wrong when you attempt to create a table.
        // Some error which says "state should be open" or something like that.
        try {
            this.database.createCollection(mongoConfig.getCollection());
        } catch (MongoCommandException exception) {
            System.out.println("[KitPvP DataManager] Database collection already exists. Skipping task.");
        }

        this.collection = database.getCollection(mongoConfig.getCollection());

        loadProfileCache().whenComplete((integer, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            System.out.println("[KitPvP DataManager] Loaded " + Utils.decimalFormat(integer) + " profiles into cache.");
        });
    }

    private CompletableFuture<Integer> loadProfileCache() {
        return CompletableFuture.supplyAsync(() -> {
            AtomicInteger loaded = new AtomicInteger(0);
            this.collection.find().forEach((Consumer<? super Document>) document -> {

                Profile profile = Profile.fromDocument(document);
                profiles.put(profile.getUniqueID(), profile);

                loaded.incrementAndGet();
            });

            return loaded.get();
        });
    }


    @Override
    public @Nullable Profile getProfile(@NotNull UUID uuid) {
        return profiles.getOrDefault(uuid, null);
    }

    @Override
    public @NotNull ProfileCreationResponse createProfile(@NotNull UUID uuid) {
        if (getProfile(uuid) != null)
            return new ProfileCreationResponse() {
                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public @Nullable Profile getCreatedProfile() {
                    return null;
                }
            };

        Profile profile = new KitPvPProfile(uuid);
        profiles.put(uuid, profile);
        return new ProfileCreationResponse() {
            @Override
            public boolean success() {
                return true;
            }

            @Override
            public @NotNull Profile getCreatedProfile() {
                return profile;
            }
        };
    }

    @Override
    public @NotNull Collection<Profile> getProfiles() {
        return Collections.unmodifiableCollection(profiles.values());
    }

    @Override
    public CompletableFuture<Void> deleteProfile(@NotNull Profile profile) {
        return CompletableFuture.runAsync(() -> {
            this.collection.deleteOne(Filters.eq("uuid", profile.getUniqueID().toString()));
            this.profiles.remove(profile.getUniqueID());
        });
    }

    @Override
    public CompletableFuture<Void> saveProfile(@NotNull Profile profile) {
        return CompletableFuture.runAsync(() -> save(profile));
    }

    @Override
    public MongoClient getClient() {
        return client;
    }

    @Override
    public MongoDatabase getDatabase() {
        return database;
    }

    @Override
    public MongoCollection<Document> getCollection() {
        return collection;
    }

    @Override
    public void saveAll() {
        profiles.values().forEach(this::save);
    }

    private void save(Profile profile) {
        Document document = profile.toDocument();
        collection.replaceOne(
                Filters.eq("uuid", profile.getUniqueID().toString()),
                document,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void closeConnection() {
        client.close();
    }

    @SneakyThrows
    private void saveConfig(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Writer writer = new FileWriter(file, false);
        gson.toJson(this.mongoConfig, writer);
        writer.flush();
        writer.close();
    }
}
