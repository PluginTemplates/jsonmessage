package io.github.portlek.jsonmessage.handle;

import org.jetbrains.annotations.NotNull;

public final class Group extends Format {

    @NotNull
    private final String id;

    @NotNull
    private final Format format;

    public Group(@NotNull final String id, @NotNull final Format format) {
        this.id = id;
        this.format = format;
    }

    @Override
    public void send(@NotNull final User user, @NotNull final String sentMessage) {
        this.format.send(user, sentMessage);
    }

}