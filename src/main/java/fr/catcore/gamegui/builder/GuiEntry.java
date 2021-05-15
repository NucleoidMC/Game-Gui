package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameConfigMetadata;
import fr.catcore.gamegui.builder.gamebuilder.CreateGameTypeEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class GuiEntry {
    private final ItemStack icon;

    protected GuiEntry(ItemStack icon) {
        this.icon = icon;
    }

    public static JoinOpenedGameEntry joinGameEntryOf(GameConfigMetadata config) {
        return new JoinOpenedGameEntry(config);
    }

    public static OpenConfiguredGameEntry openConfiguredGameEntryOf(GameConfigMetadata config) {
        return new OpenConfiguredGameEntry(config);
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
