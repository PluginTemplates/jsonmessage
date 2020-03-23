package io.github.portlek.jsonmessage.file.provider;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.portlek.jsonmessage.util.FileElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class UserMenuProvider implements InventoryProvider {

    @NotNull
    public final FileElement chosen;

    @NotNull
    public final FileElement dark_red;

    @NotNull
    public final FileElement red;

    @NotNull
    public final FileElement gold;

    @NotNull
    public final FileElement yellow;

    @NotNull
    public final FileElement dark_green;

    @NotNull
    public final FileElement green;

    @NotNull
    public final FileElement aqua;

    @NotNull
    public final FileElement dark_aqua;

    @NotNull
    public final FileElement dark_blue;

    @NotNull
    public final FileElement blue;

    @NotNull
    public final FileElement light_purple;

    @NotNull
    public final FileElement dark_purple;

    @NotNull
    public final FileElement white;

    @NotNull
    public final FileElement gray;

    @NotNull
    public final FileElement dark_gray;

    @NotNull
    public final FileElement black;

    @NotNull
    public final FileElement bold;

    @NotNull
    public final FileElement strikethrough;

    @NotNull
    public final FileElement underline;

    @NotNull
    public final FileElement italic;

    @NotNull
    public final FileElement reset;

    public UserMenuProvider(@NotNull final FileElement chosen,
                            @NotNull final FileElement dark_red,
                            @NotNull final FileElement red,
                            @NotNull final FileElement gold,
                            @NotNull final FileElement yellow,
                            @NotNull final FileElement dark_green,
                            @NotNull final FileElement green,
                            @NotNull final FileElement aqua,
                            @NotNull final FileElement dark_aqua,
                            @NotNull final FileElement dark_blue,
                            @NotNull final FileElement blue,
                            @NotNull final FileElement light_purple,
                            @NotNull final FileElement dark_purple,
                            @NotNull final FileElement white,
                            @NotNull final FileElement gray,
                            @NotNull final FileElement dark_gray,
                            @NotNull final FileElement black,
                            @NotNull final FileElement bold,
                            @NotNull final FileElement strikethrough,
                            @NotNull final FileElement underline,
                            @NotNull final FileElement italic,
                            @NotNull final FileElement reset) {
        this.chosen = chosen;
        this.dark_red = dark_red;
        this.red = red;
        this.gold = gold;
        this.yellow = yellow;
        this.dark_green = dark_green;
        this.green = green;
        this.aqua = aqua;
        this.dark_aqua = dark_aqua;
        this.dark_blue = dark_blue;
        this.blue = blue;
        this.light_purple = light_purple;
        this.dark_purple = dark_purple;
        this.white = white;
        this.gray = gray;
        this.dark_gray = dark_gray;
        this.black = black;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underline = underline;
        this.italic = italic;
        this.reset = reset;
    }

    @Override
    public void init(final Player player, final InventoryContents contents) {

    }

    @Override
    public void update(final Player player, final InventoryContents contents) {
    }

}
