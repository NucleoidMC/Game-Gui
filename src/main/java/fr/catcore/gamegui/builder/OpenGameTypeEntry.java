package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.inventory.OpenConfiguredGameInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class OpenGameTypeEntry extends GuiEntry {
    private Identifier gameType;

    protected OpenGameTypeEntry(ItemStack icon) {
        super(icon);
    }

    public OpenGameTypeEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(GameGui.getGameTypeName(this.gameType));
        for (Text text : GameGui.getGameTypeDescription(this.gameType)) {
            builder.addLore(text);
        }
        return builder.build().copy();
    }

    public void onClick(ServerPlayerEntity player) {
        OpenConfiguredGameInventory inventory = new OpenConfiguredGameInventory(player, openConfiguredGameBuilder -> {
            Identifier[] configs = GameGui.getConfigsFromType(this.gameType);
            for (Identifier configuredGame : configs) {
                openConfiguredGameBuilder.add(GuiEntry
                        .openConfiguredGameEntryOf(GameGui.getGameInfos(this.gameType).get())
                        .withGameConfig(configuredGame));
            }
        });
        ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(inventory, player);
    }
}
