package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.builder.gamebuilder.CreateGameTypeEntry;
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

    public static OpenConfiguredGameEntry openConfiguredGameEntryOf(ItemStack icon) {
        return new OpenConfiguredGameEntry(icon);
    }

    public static OpenConfiguredGameEntry openConfiguredGameEntryOf(Item icon) {
        return openConfiguredGameEntryOf(new ItemStack(icon));
    }

    public static OpenGameTypeEntry openGameTypeEntryOf(ItemStack icon) {
        return new OpenGameTypeEntry(icon);
    }

    public static OpenGameTypeEntry openGameTypeEntryOf(Item icon) {
        return openGameTypeEntryOf(new ItemStack(icon));
    }

    public static CreateGameTypeEntry createGameTypeEntryOf(ItemStack icon) {
        return new CreateGameTypeEntry(icon);
    }

    public static CreateGameTypeEntry createGameTypeEntryOf(Item icon) {
        return createGameTypeEntryOf(new ItemStack(icon));
    }

    public ItemStack getIcon() {
        return icon;
    }

    public abstract ItemStack createIcon(ServerPlayerEntity player);

    public abstract void onClick(ServerPlayerEntity player);
}
