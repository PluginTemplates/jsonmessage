package io.github.portlek.jsonmessage.file;

import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.util.Replaceable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = @Config(
    name = "en",
    location = "%basedir%/JsonMessage/languages"
))
public final class LanguageFile extends BukkitLinkedManaged {

    @Instance
    public LanguageFile.Error errors = new Error();

    @Instance
    public LanguageFile.General generals = new General();

    @Value
    public Replaceable<List<String>> help_messages = this.match(s -> {
        if ("en".equals(s)) {
            return Optional.of(
                Replaceable.of(
                    "&a====== %prefix% &a======",
                    "&7/jsonmessage &r> &eShows help message.",
                    "&7/jsonmessage help &r> &eShows help message.",
                    "&7/jsonmessage reload &r> &eReloads the plugin."
                )
                    .map(ColorUtil::colored)
                    .replace(this.getPrefix())
            );
        }

        return Optional.empty();
    });

    public LanguageFile(@NotNull final ConfigFile configFile) {
        super(configFile.plugin_language, MapEntry.from("config", configFile));
    }

    @NotNull
    public Map<String, Supplier<String>> getPrefix() {
        final Map<String, Supplier<String>> map = new HashMap<>();
        this.pull("config").ifPresent(o ->
            map.put("%prefix%", () -> ((ConfigFile) o).plugin_prefix.build())
        );
        return map;
    }

    @Section(path = "error")
    public class Error {

        @Value
        public Replaceable<String> player_not_found = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.of("%prefix% &cPlayer not found! (%player%)")
                        .map(ColorUtil::colored)
                        .replaces("%player%")
                        .replace(LanguageFile.this.getPrefix())
                );
            }
            return Optional.empty();
        });

    }

    @Section(path = "general")
    public class General {

        @Value
        public Replaceable<String> reload_complete = LanguageFile.this.match(s -> {
            if ("en".equals(s)) {
                return Optional.of(
                    Replaceable.of("%prefix% &aReload complete! &7Took (%ms%ms)")
                        .map(ColorUtil::colored)
                        .replace(LanguageFile.this.getPrefix())
                        .replaces("%ms%")
                );
            }
            return Optional.empty();
        });

    }

}
