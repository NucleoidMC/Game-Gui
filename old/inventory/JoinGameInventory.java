package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.JoinGameBuilder;
import fr.catcore.gamegui.builder.JoinGameEntry;
import fr.catcore.gamegui.builder.JoinOpenedGameBuilder;
import fr.catcore.gamegui.builder.JoinOpenedGameEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public class JoinGameInventory extends GameGUIInventory<JoinGameBuilder, JoinGameEntry> {

    public JoinGameInventory(ServerPlayerEntity player) {
        super(player, joinOpenedGameBuilder -> {
            joinOpenedGameBuilder.add(new JoinGameEntry(JoinGameEntry.Type.JOIN_OPENED));
            joinOpenedGameBuilder.add(new JoinGameEntry(JoinGameEntry.Type.JOIN_CHANNEL));
        });
    }
}
