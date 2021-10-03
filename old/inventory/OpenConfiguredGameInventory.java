package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.builder.OpenConfiguredGameBuilder;
import fr.catcore.gamegui.builder.PageGuiEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
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
        this.pageElements.set(49, new PageGuiEntry(ItemStackBuilder.of(Items.CRYING_OBSIDIAN).setName(new TranslatableText("text.game_gui.gui.back")).build()) {
            @Override
            public void onClick(ServerPlayerEntity player) {
                OpenGameTypeInventory typeInventory = new OpenGameTypeInventory(player, openGameTypeBuilder -> {
                    for (GameType<?> gameType : GameType.REGISTRY.values()) {
                        if (!GameGui.hasConfigsForType(gameType)) {
                            continue;
                        }

                        ItemStack icon = GameGui.getGameInfos(gameType.getIdentifier()).get();
                        openGameTypeBuilder.add(GuiEntry.openGameTypeEntryOf(icon).withGameType(gameType));
                    }
                }, OpenConfiguredGameInventory.this.typePage);
                ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(typeInventory, player);
            }
        });
    }
}
