package gg.souppvp.kitpvp.api.model.response;

import gg.souppvp.kitpvp.api.model.Profile;
import org.jetbrains.annotations.Nullable;

public interface ProfileCreationResponse {

    boolean success();

    @Nullable
    Profile getCreatedProfile();

}
