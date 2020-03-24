package io.github.portlek.jsonmessage.handle;

import io.github.portlek.mcjson.api.JsonCompound;
import io.github.portlek.mcjson.api.JsonFeature;
import io.github.portlek.mcjson.base.compound.BasicJsonCompound;
import java.util.List;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

public final class Message {

    @NotNull
    private final BiFunction<User, String, String> format;

    @NotNull
    private final BiFunction<User, String, List<JsonFeature>> features;

    public Message(@NotNull final BiFunction<User, String, String> format,
                   @NotNull final BiFunction<User, String, List<JsonFeature>> features) {
        this.format = format;
        this.features = features;
    }

    @NotNull
    public JsonCompound toCompound(@NotNull final User user, @NotNull final String sentMessage) {
        return new BasicJsonCompound(
            this.format.apply(user, sentMessage),
            this.features.apply(user, sentMessage)
        );
    }

}