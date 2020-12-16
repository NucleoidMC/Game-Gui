package fr.catcore.gamegui.builder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class GuiEntry {
    private final ItemStack icon;

    protected GuiEntry(ItemStack icon) {
        this.icon = icon;
    }

    public static JoinGameEntry joinGameEntryOf(ItemStack icon) {
        return new JoinGameEntry(icon);
    }

    public static JoinGameEntry joinGameEntryOf(Item icon) {
        return joinGameEntryOf(new ItemStack(icon));
    }

    public ItemStack getIcon() {
        return icon;
    }

    public abstract ItemStack createIcon(ServerPlayerEntity player);

    public abstract void onClick(ServerPlayerEntity player);
}
