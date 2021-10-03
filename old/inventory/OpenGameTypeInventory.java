package fr.catcore.gamegui.inventory;


import fr.catcore.gamegui.builder.OpenGameTypeBuilder;
import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public class OpenGameTypeInventory extends PagedGameGUIInventory<OpenGameTypeBuilder> {

    public OpenGameTypeInventory(ServerPlayerEntity player, Consumer<OpenGameTypeBuilder> builder, int page) {
        super(player, builder, page);
    }

    @Override
    public void addAdditionalEntry() {

    }
}
