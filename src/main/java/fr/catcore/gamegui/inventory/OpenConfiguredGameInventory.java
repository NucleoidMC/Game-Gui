package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.builder.OpenConfiguredGameBuilder;
import fr.catcore.gamegui.builder.OpenConfiguredGameEntry;
import fr.catcore.gamegui.builder.PageGuiEntry;
import fr.catcore.gamegui.item.OpenGameItem;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.function.Consumer;

public class OpenConfiguredGameInventory extends PagedGameGUIInventory<OpenConfiguredGameBuilder> {

    private final int typePage;

    public OpenConfiguredGameInventory(ServerPlayerEntity player, Consumer<OpenConfiguredGameBuilder> builder, int typePage) {
        super(player, builder, 0);
        this.typePage = typePage;
    }

    @Override
    public void addAdditionalEntry() {
        this.pageElements.set(49, new PageGuiEntry(ItemStackBuilder.of(Items.CRYING_OBSIDIAN).setName(new LiteralText("Back")).build()) {
            @Override
            public void onClick(ServerPlayerEntity player) {
                OpenGameTypeInventory typeInventory = new OpenGameTypeInventory(player, openGameTypeBuilder -> {
                    for (Identifier gameType : GameType.REGISTRY.keySet()) {
                        if (gameType.toString().equals("plasmid:test")) continue;
                        openGameTypeBuilder.add(GuiEntry.openGameTypeEntryOf(GameGui.getGameInfos(gameType).get()).withGameType(gameType));
                    }
                }, OpenConfiguredGameInventory.this.typePage);
                ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(typeInventory, player);
            }
        });
    }
}
