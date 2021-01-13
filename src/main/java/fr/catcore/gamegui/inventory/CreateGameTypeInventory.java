package fr.catcore.gamegui.inventory;


import fr.catcore.gamegui.builder.OpenGameTypeBuilder;
import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import fr.catcore.gamegui.builder.gamebuilder.CreateGameTypeBuilder;
import fr.catcore.gamegui.builder.gamebuilder.CreateGameTypeEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public class CreateGameTypeInventory extends GameGUIInventory<CreateGameTypeBuilder, CreateGameTypeEntry> {

    public CreateGameTypeInventory(ServerPlayerEntity player, Consumer<CreateGameTypeBuilder> builder) {
        super(player, builder);
    }
}
