package fr.catcore.gamegui.accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ForgingScreenHandlerAccessor {

    ItemStack getOutput();

    CraftingResultInventory getCraftingResult();

    Inventory getInventory();

    void setInventory(Inventory inventory, ServerPlayerEntity playerEntity);

    boolean canTakeOutput_public(PlayerEntity player, boolean present);

    ItemStack onTakeOutput_public(PlayerEntity player, ItemStack stack);
}
