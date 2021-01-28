package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameConfigMetadata;
import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.inventory.OpenConfiguredGameInventory;
import fr.catcore.gamegui.inventory.OpenGameTypeInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class OpenGameTypeEntry extends GuiEntry {
    private GameType<?> gameType;

    protected OpenGameTypeEntry(ItemStack icon) {
        super(icon);
    }

    public OpenGameTypeEntry withGameType(GameType<?> gameType) {
        this.gameType = gameType;
        return this;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(this.gameType.getName());
        for (Text text : GameGui.getGameTypeDescription(this.gameType.getIdentifier())) {
            builder.addLore(text);
        }
        return builder.build().copy();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        Inventory typeInventory = ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).getInventory();
        OpenGameTypeInventory openGameTypeInventory = (OpenGameTypeInventory) typeInventory;
        OpenConfiguredGameInventory configuredInventory = new OpenConfiguredGameInventory(player, openConfiguredGameBuilder -> {
            ConfiguredGame<?>[] configs = GameGui.getConfigsFromType(this.gameType);
            for (ConfiguredGame<?> game : configs) {
                openConfiguredGameBuilder.add(GuiEntry.openConfiguredGameEntryOf(GameConfigMetadata.parse(game)));
            }
        }, openGameTypeInventory.getPageIndex());
        ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(configuredInventory, player);
    }
}
