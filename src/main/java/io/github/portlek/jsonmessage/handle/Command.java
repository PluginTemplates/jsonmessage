package io.github.portlek.jsonmessage.handle;

import io.github.portlek.jsonmessage.JsonMessage;
import java.util.List;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Command {

    private final boolean asPlayer;

    @NotNull
    private final Function<Player, List<String>> commands;

    public Command(final boolean asPlayer, @NotNull final Function<Player, List<String>> commands) {
        this.asPlayer = asPlayer;
        this.commands = commands;
    }

    public void run(@NotNull final Player clicker) {
        this.commands.apply(clicker).forEach(command -> Bukkit.getScheduler().callSyncMethod(JsonMessage.getInstance(),
            () -> {
                if (this.asPlayer) {
                    return clicker.performCommand(command);
                }
                return Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            })
        );
    }

}