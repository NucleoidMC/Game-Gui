package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.OpenConfiguredGameBuilder;
import fr.catcore.gamegui.builder.OpenConfiguredGameEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.function.Consumer;

public class OpenConfiguredGameInventory extends GameGUIInventory<OpenConfiguredGameBuilder, OpenConfiguredGameEntry> {

    public OpenConfiguredGameInventory(ServerPlayerEntity player, Consumer<OpenConfiguredGameBuilder> builder) {
        super(player, builder);
    }
}
