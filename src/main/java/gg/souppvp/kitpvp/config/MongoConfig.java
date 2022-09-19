package gg.souppvp.kitpvp.config;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class MongoConfig {

    @SerializedName("host")
    private String domain = "data01.negative.games";

    @SerializedName("username")
    private String user = "username";

    @SerializedName("password")
    private String pass = "password";

    @SerializedName("database")
    private String database = "souppvp";

    @SerializedName("port")
    private String port = "27017";

    @SerializedName("collection-name")
    private String collection = "user-data";
}
