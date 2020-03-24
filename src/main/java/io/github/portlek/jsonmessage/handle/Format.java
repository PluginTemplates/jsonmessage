package io.github.portlek.jsonmessage.handle;

import io.github.portlek.mcjson.base.message.BasicJsonMessage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class Format {

    @NotNull
    private final List<Message> messages;

    public Format(@NotNull final Message... messages) {
        this(Arrays.asList(messages));
    }

    public Format(@NotNull final List<Message> messages) {
        this.messages = messages;
    }

    public void send(@NotNull final User user, @NotNull final String sentMessage) {
        new BasicJsonMessage(
            this.messages.stream()
                .map(message -> message.toCompound(user, sentMessage))
                .collect(Collectors.toList())
        ).sendAll();
    }

}