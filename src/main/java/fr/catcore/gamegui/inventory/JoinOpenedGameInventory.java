package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.JoinOpenedGameBuilder;
import fr.catcore.gamegui.builder.PageGuiEntry;
import fr.catcore.gamegui.item.JoinGameItem;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.function.Consumer;

public class JoinOpenedGameInventory extends PagedGameGUIInventory<JoinOpenedGameBuilder> {

    public JoinOpenedGameInventory(ServerPlayerEntity player, Consumer<JoinOpenedGameBuilder> builder) {
        super(player, builder, 0);
    }

    @Override
    public void addAdditionalEntry() {
        this.pageElements.set(49, new PageGuiEntry(ItemStackBuilder.of(Items.CRYING_OBSIDIAN).setName(new TranslatableText("text.game_gui.gui.back")).build()) {
            @Override
            public void onClick(ServerPlayerEntity player) {
                JoinGameItem.openJoinScreen(player);
            }
        });
    }
}
