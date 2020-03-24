package io.github.portlek.jsonmessage.handle;

import io.github.portlek.versionmatched.Version;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

public final class HandItem implements Scalar<ItemStack> {

    private final Version version = new Version();

    @NotNull
    private final Player player;

    public HandItem(@NotNull final Player player) {
        this.player = player;
    }

    @Override
    public ItemStack value() {
        if (this.version.minor() < 9) {
            return this.player.getItemInHand();
        }

        return this.player.getInventory().getItemInMainHand();
    }

}