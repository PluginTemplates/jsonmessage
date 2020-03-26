package io.github.portlek.jsonmessage.handle;

import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.itemstack.util.Colored;
import io.github.portlek.jsonmessage.JsonMessage;
import io.github.portlek.jsonmessage.Wrapped;
import io.github.portlek.jsonmessage.hooks.PlaceholderAPIWrapper;
import io.github.portlek.mcjson.api.JsonFeature;
import io.github.portlek.mcjson.base.event.click.*;
import io.github.portlek.mcjson.base.event.hover.ShowAchievement;
import io.github.portlek.mcjson.base.event.hover.ShowEntity;
import io.github.portlek.mcjson.base.event.hover.ShowItem;
import io.github.portlek.mcjson.base.event.hover.ShowText;
import io.github.portlek.mcjson.base.feature.*;
import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public final class FormatFile {

    @NotNull
    private final BukkitManaged managed;

    public FormatFile(@NotNull final BukkitManaged managed) {
        this.managed = managed;
    }

    @NotNull
    public BukkitManaged getManaged() {
        return this.managed;
    }

    @NotNull
    public List<Message> loadMessages() {
        return this.loadMessages(false);
    }

    @NotNull
    public List<Message> loadMessages(final boolean ismessage) {
        final List<Message> messages = new ArrayList<>();
        this.managed.getOrCreateSection("components").ifPresent(section ->
            section.getKeys(false).forEach(key ->
                messages.add(new Message(
                    (user, message) -> this.replaceAll(user, this.managed.getOrSet("components." + key + ".text", ""), message, ismessage),
                    (user, message) -> this.parseFeatures(user, message, key, ismessage)
                ))
            )
        );
        return messages;
    }

    @NotNull
    private String replaceAll(@NotNull final User user, @NotNull final String text, @NotNull final String message,
                              final boolean ismessage) {
        final Optional<Player> optional = user.getPlayer();
        if (!optional.isPresent()) {
            return "";
        }
        final Optional<Wrapped> wrapped = JsonMessage.getAPI().configFile.getWrapped("PlaceholderAPI");
        final String finalMessage;
        if (wrapped.isPresent() && wrapped.get() instanceof PlaceholderAPIWrapper) {
            finalMessage = ((PlaceholderAPIWrapper) wrapped.get()).apply(
                optional.get(),
                text
            );
        } else {
            finalMessage = text;
        }
        if (user.rainbow() && ismessage) {
            return this.rainbow(this.replaceAll(finalMessage, optional.get(), message));
        }
        return this.replaceAll(finalMessage, optional.get(), message);
    }

    @NotNull
    private List<JsonFeature> parseFeatures(@NotNull final User user, @NotNull final String message,
                                            @NotNull final String key, final boolean ismessage) {
        final Optional<Player> userOptional = user.getPlayer();
        if (!userOptional.isPresent()) {
            return Collections.emptyList();
        }
        final String path = "components." + key + '.';
        final List<JsonFeature> features = new ArrayList<>();
        final String color;
        if (ismessage && user.color()) {
            color = user.getColorName();
        } else {
            color = this.managed.getOrSet(path + "color", "");
        }
        final String insertion = this.managed.getOrSet(path + "insertion", "");
        final boolean underlined = ismessage && user.underlined() || this.managed.getOrSet(path + "underlined", false);
        final boolean strikethrough = ismessage && user.strikethrough() || this.managed.getOrSet(path + "strikethrough", false);
        final boolean obfuscated = this.managed.getOrSet(path + "obfuscated", false);
        final boolean italic = ismessage && user.italic() || this.managed.getOrSet(path + "italic", false);
        final boolean bold = ismessage && user.bold() || this.managed.getOrSet(path + "bold", false);
        if (!color.isEmpty()) {
            features.add(new ColorFeature(color));
        }
        if (!insertion.isEmpty()) {
            features.add(new InsertionFeature(insertion));
        }
        if (underlined) {
            features.add(new UnderlinedFeature(true));
        }
        if (strikethrough) {
            features.add(new StrikethroughFeature(true));
        }
        if (obfuscated) {
            features.add(new ObfuscatedFeature(true));
        }
        if (italic) {
            features.add(new ItalicFeature(true));
        }
        if (bold) {
            features.add(new BoldFeature(true));
        }
        this.managed.getSection(path + "hover").ifPresent(section -> {
            final String action = this.managed.getOrSet(path + "hover.action", "").toLowerCase(Locale.ENGLISH);
            if ("show_text".equalsIgnoreCase(action)) {
                features.add(
                    new HoverFeature(
                        new ShowText(
                            this.replaceAll(user, this.managed.getOrSet(path + "hover.value", ""), message)
                        )
                    )
                );
            } else if ("show_achievement".equalsIgnoreCase(action)) {
                features.add(
                    new HoverFeature(
                        new ShowAchievement(
                            this.replaceAll(user, this.managed.getOrSet(path + "hover.value", ""), message)
                        )
                    )
                );
            } else if ("show_item".equalsIgnoreCase(action)) {
                final Optional<ConfigurationSection> optional = this.managed.getSection(path + "hover.value");
                final ItemStack itemStack;
                if (optional.isPresent()) {
                    itemStack = this.managed.getItemStack(path + "hover.value").orElse(null);
                } else {
                    final String itemValue = this.managed.getOrSet(path + "hover.value", "");
                    if ("%player_hand%".equalsIgnoreCase(itemValue)) {
                        itemStack = new HandItem(userOptional.get()).value();
                    } else {
                        itemStack = new ItemStack(Material.AIR);
                    }
                }
                if (itemStack != null) {
                    features.add(
                        new HoverFeature(
                            new ShowItem(itemStack)
                        )
                    );
                }
            } else if ("show_entity".equalsIgnoreCase(action)) {
                final Optional<ConfigurationSection> optional = this.managed.getSection(path + "hover.value");
                final int id;
                final String name;
                final String type;
                if (optional.isPresent()) {
                    id = this.managed.getOrSet(path + "hover.value.id", 0);
                    name = this.managed.getOrSet(path + "hover.value.name", "");
                    type = this.managed.getOrSet(path + "hover.value.type", "");
                } else {
                    id = 0;
                    name = "Unknown Entity";
                    type = "minecraft:ping";
                }
                features.add(
                    new HoverFeature(
                        new ShowEntity(id, name, type)
                    )
                );
            }
        });
        this.managed.getSection(path + "click").ifPresent(section -> {
            final String action = this.managed.getOrSet(path + "click.action", "").toLowerCase(Locale.ENGLISH);
            if ("run_consumer".equalsIgnoreCase(action)) {
                final List<Command> commands = new ArrayList<>();
                this.managed.getSection(path + "click.commands").ifPresent(clickSection ->
                    clickSection.getKeys(false).forEach(s ->
                        commands.add(
                            new Command(
                                this.managed.getOrSet(path + "click.commands." + s + ".as-player", false),
                                clicker ->
                                    this.managed.getStringList(path + "click.commands." + s + ".values").stream()
                                        .map(value ->
                                            this.replaceAllClick(
                                                "clicker".equalsIgnoreCase(this.managed.getOrSet(
                                                    path + "click.commands." + value + ".placeholder-mode",
                                                    "clicker"
                                                )),
                                                clicker,
                                                user,
                                                value,
                                                message
                                            )
                                        ).collect(Collectors.toList())
                            )
                        )
                    )
                );
                features.add(
                    new ClickFeature(
                        new RunConsumer(
                            JsonMessage.getInstance(),
                            this.managed.getOrSet(path + "click.remove-after-click", true),
                            clicker -> commands.forEach(
                                command -> command.run(userOptional.get())
                            )
                        )
                    )
                );
            }
            final String value = ColorUtil.colored(
                this.managed.getOrSet(path + "click.value", "")
            );
            if ("run_command".equalsIgnoreCase(action)) {
                features.add(
                    new ClickFeature(
                        new RunCommand(this.replaceAll(user, value, message))
                    )
                );
            } else if ("suggest_command".equalsIgnoreCase(action)) {
                features.add(
                    new ClickFeature(
                        new SuggestCommand(this.replaceAll(user, value, message))
                    )
                );
            } else if ("change_page".equalsIgnoreCase(action)) {
                features.add(
                    new ClickFeature(
                        new ChangePage(this.replaceAll(user, value, message))
                    )
                );
            } else if ("open_url".equalsIgnoreCase(action)) {
                features.add(
                    new ClickFeature(
                        new OpenURL(this.replaceAll(user, value, message))
                    )
                );
            }
        });
        return features;
    }

    @NotNull
    private String replaceAll(@NotNull final String text, @NotNull final Player player, @NotNull final String message) {
        return new Colored(
            text
                .replace("%writer%", player.getName())
                .replace("%player%", player.getName())
                .replace("%player_name%", player.getName())
                .replace("%message%", message)
        ).value();
    }

    @NotNull
    public String rainbow(@NotNull final String message) {
        final char[] colors = JsonMessage.getAPI().configFile.rainbow_sequence.toCharArray();
        final char[] msgchars = message.toCharArray();
        int currentColorIndex = 0;
        final StringBuilder sb = new StringBuilder();
        for (final char msgchar : msgchars) {
            if (currentColorIndex == colors.length) {
                currentColorIndex = 0;
            }
            if (msgchar == ' ') {
                sb.append(' ');
            } else {
                sb.append('&').append(colors[currentColorIndex]).append(msgchar);
                currentColorIndex++;
            }
        }
        return ColorUtil.colored(sb.toString());
    }

    @NotNull
    private String replaceAll(@NotNull final User user, @NotNull final String text, @NotNull final String message) {
        return this.replaceAll(user, text, message, false);
    }

    @NotNull
    private String replaceAllClick(final boolean clickerMode, @NotNull final Player clicker,
                                   @NotNull final User user, @NotNull final String command,
                                   @NotNull final String message) {
        final Optional<Player> optional = user.getPlayer();
        if (!optional.isPresent()) {
            return "";
        }
        final Optional<Wrapped> wrapped = JsonMessage.getAPI().configFile.getWrapped("PlaceholderAPI");
        final String finalMessage;
        if (wrapped.isPresent() && wrapped.get() instanceof PlaceholderAPIWrapper) {
            finalMessage = ((PlaceholderAPIWrapper) wrapped.get()).apply(
                clickerMode ? clicker : optional.get(),
                command
            );
        } else {
            finalMessage = command;
        }
        return this.replaceAll(
            finalMessage
                .replace("%player_name%", optional.get().getName())
                .replace("%clicker%", clicker.getName()),
            optional.get(),
            message
        );
    }

}
