package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.JoinGameBuilder;
import fr.catcore.gamegui.builder.JoinGameEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.function.Consumer;

public class JoinGameInventory extends GameGUIInventory<JoinGameBuilder, JoinGameEntry> {

    public JoinGameInventory(ServerPlayerEntity player, Consumer<JoinGameBuilder> builder) {
        super(player, builder);
    }
}
