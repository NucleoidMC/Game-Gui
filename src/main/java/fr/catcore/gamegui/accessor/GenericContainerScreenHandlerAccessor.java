package fr.catcore.gamegui.accessor;

import net.minecraft.inventory.Inventory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface GenericContainerScreenHandlerAccessor {

    void setInventory(Inventory inventory, ServerPlayerEntity playerEntity);

    Inventory getInventory();
}
