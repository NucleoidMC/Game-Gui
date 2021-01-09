package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.accessor.ForgingScreenHandlerAccessor;
import fr.catcore.gamegui.accessor.ScreenHandlerAccessor;
import fr.catcore.gamegui.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ForgingScreenHandler.class)
public abstract class MixinForgingScreenHandler implements ForgingScreenHandlerAccessor {
    @Shadow @Final protected CraftingResultInventory output;

    @Mutable
    @Shadow @Final protected Inventory input;

    @Shadow protected abstract boolean canTakeOutput(PlayerEntity player, boolean present);

    @Shadow protected abstract ItemStack onTakeOutput(PlayerEntity player, ItemStack stack);

    @Override
    public ItemStack getOutput() {
        return this.output.getStack(0);
    }

    @Override
    public Inventory getInventory() {
        return this.input;
    }

    public boolean canTakeOutput_public(PlayerEntity player, boolean present) {
        return this.canTakeOutput(player, present);
    }

    @Override
    public CraftingResultInventory getCraftingResult() {
        return this.output;
    }

    public ItemStack onTakeOutput_public(PlayerEntity player, ItemStack stack) {
        return this.onTakeOutput(player, stack);
    }

    @Override
    public void setInventory(Inventory inventory, ServerPlayerEntity playerEntity) {
        this.input = inventory;
        ((ScreenHandler)(Object)this).slots.clear();
        ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(this.input, 0, 27, 47));
        ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(this.input, 1, 76, 47));
        ((ScreenHandlerAccessor)(Object)this).addSlot_public(Utils.createAnonymousSlot((ForgingScreenHandler) (Object)this));

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(playerEntity.inventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(playerEntity.inventory, k, 8 + k * 18, 142));
        }
    }
}
