package io.github.portlek.jsonmessage.file;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.SmartInventory;
import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.BukkitItemBuilder;
import io.github.portlek.configs.util.ColorUtil;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.Replaceable;
import io.github.portlek.jsonmessage.JsonMessage;
import io.github.portlek.jsonmessage.file.provider.UserMenuProvider;
import io.github.portlek.jsonmessage.util.FileElement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Config(
    name = "menu",
    type = FileType.JSON,
    location = "%basedir%/JsonMessage"
)
public final class MenuFile extends BukkitManaged {

    @Instance
    public final MenuFile.UserMenu userMenu = new MenuFile.UserMenu();

    @Override
    public void load() {
        this.addCustomValue(FileElement.class, new FileElement.Provider());
        super.load();
        this.setAutoSave(true);
    }

    @Section(path = "user-menu")
    public static final class UserMenu {

        @Instance
        public final MenuFile.UserMenu.Elements elements = new MenuFile.UserMenu.Elements();

        @Value
        public Replaceable<String> title = Replaceable.of("%player_name%")
            .map(ColorUtil::colored)
            .replaces("%player_name%");

        @Value
        public int size = 54;

        @NotNull
        public SmartInventory inventory(@NotNull final Player player) {
            return SmartInventory.builder()
                .id("user-menu")
                .closeable(true)
                .size(this.size / 9, 9)
                .title(this.title
                    .build("%player_name%", player::getName))
                .manager(JsonMessage.getAPI().inventoryManager)
                .provider(
                    new UserMenuProvider(
                        this.elements.chosen,
                        this.elements.dark_red,
                        this.elements.red,
                        this.elements.gold,
                        this.elements.yellow,
                        this.elements.dark_green,
                        this.elements.green,
                        this.elements.aqua,
                        this.elements.dark_aqua,
                        this.elements.dark_blue,
                        this.elements.blue,
                        this.elements.light_purple,
                        this.elements.dark_purple,
                        this.elements.white,
                        this.elements.gray,
                        this.elements.dark_gray,
                        this.elements.black,
                        this.elements.bold,
                        this.elements.strikethrough,
                        this.elements.underline,
                        this.elements.italic,
                        this.elements.reset
                    )
                ).build();
        }

        @Section(path = "elements")
        public static final class Elements {

            @Value
            public FileElement chosen = new FileElement(
                BukkitItemBuilder.of(XMaterial.GREEN_DYE)
                    .name(" ")
                    .build(),
                0, 0
            );

            @Value
            public FileElement dark_red = new FileElement(
                BukkitItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                    .name("&4Dark Red")
                    .build(),
                1, 1
            );

            @Value
            public FileElement red = new FileElement(
                BukkitItemBuilder.of(XMaterial.RED_STAINED_GLASS_PANE)
                    .name("&cRed")
                    .build(),
                1, 2
            );

            @Value
            public FileElement gold = new FileElement(
                BukkitItemBuilder.of(XMaterial.GOLD_BLOCK)
                    .name("&6Gold")
                    .build(),
                1, 3
            );

            @Value
            public FileElement yellow = new FileElement(
                BukkitItemBuilder.of(XMaterial.YELLOW_STAINED_GLASS_PANE)
                    .name("&eYellow")
                    .build(),
                1, 4
            );

            @Value
            public FileElement dark_green = new FileElement(
                BukkitItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                    .name("&2Dark Green")
                    .build(),
                1, 5
            );

            @Value
            public FileElement green = new FileElement(
                BukkitItemBuilder.of(XMaterial.GREEN_STAINED_GLASS_PANE)
                    .name("&aGreen")
                    .build(),
                1, 6
            );

            @Value
            public FileElement aqua = new FileElement(
                BukkitItemBuilder.of(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .name("&bAqua")
                    .build(),
                1, 7
            );

            @Value
            public FileElement dark_aqua = new FileElement(
                BukkitItemBuilder.of(XMaterial.BLUE_STAINED_GLASS_PANE)
                    .name("&3Dark Aqua")
                    .build(),
                2, 0
            );

            @Value
            public FileElement dark_blue = new FileElement(
                BukkitItemBuilder.of(XMaterial.BLUE_STAINED_GLASS_PANE)
                    .name("&1Dark Blue")
                    .build(),
                2, 1
            );

            @Value
            public FileElement blue = new FileElement(
                BukkitItemBuilder.of(XMaterial.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .name("&9Blue")
                    .build(),
                2, 2
            );

            @Value
            public FileElement light_purple = new FileElement(
                BukkitItemBuilder.of(XMaterial.PURPLE_STAINED_GLASS_PANE)
                    .name("&dLight Purple")
                    .build(),
                2, 3
            );

            @Value
            public FileElement dark_purple = new FileElement(
                BukkitItemBuilder.of(XMaterial.PURPLE_STAINED_GLASS_PANE)
                    .name("&5Dark Purple")
                    .build(),
                2, 4
            );

            @Value
            public FileElement white = new FileElement(
                BukkitItemBuilder.of(XMaterial.WHITE_STAINED_GLASS_PANE)
                    .name("&fWhite")
                    .build(),
                2, 5
            );

            @Value
            public FileElement gray = new FileElement(
                BukkitItemBuilder.of(XMaterial.LIGHT_GRAY_STAINED_GLASS_PANE)
                    .name("&7Gray")
                    .build(),
                2, 6
            );

            @Value
            public FileElement dark_gray = new FileElement(
                BukkitItemBuilder.of(XMaterial.GRAY_STAINED_GLASS_PANE)
                    .name("&8Dark Gray")
                    .build(),
                2, 7
            );

            @Value
            public FileElement black = new FileElement(
                BukkitItemBuilder.of(XMaterial.BLACK_STAINED_GLASS_PANE)
                    .name("&9Blue")
                    .build(),
                3, 1
            );

            @Value
            public FileElement bold = new FileElement(
                BukkitItemBuilder.of(XMaterial.ORANGE_DYE)
                    .name("&bBold")
                    .build(),
                4, 3
            );

            @Value
            public FileElement strikethrough = new FileElement(
                BukkitItemBuilder.of(XMaterial.ORANGE_DYE)
                    .name("&mStrikethrough")
                    .build(),
                4, 4
            );

            @Value
            public FileElement underline = new FileElement(
                BukkitItemBuilder.of(XMaterial.ORANGE_DYE)
                    .name("&nUnderline")
                    .build(),
                4, 5
            );

            @Value
            public FileElement italic = new FileElement(
                BukkitItemBuilder.of(XMaterial.ORANGE_DYE)
                    .name("&oItalic")
                    .build(),
                4, 6
            );

            @Value
            public FileElement reset = new FileElement(
                BukkitItemBuilder.of(XMaterial.ORANGE_DYE)
                    .name("&rReset")
                    .build(),
                4, 7
            );

        }

    }

}
