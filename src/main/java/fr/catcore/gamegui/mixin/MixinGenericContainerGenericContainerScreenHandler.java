package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.accessor.ScreenHandlerAccessor;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GenericContainerScreenHandler.class)
public class MixinGenericContainerGenericContainerScreenHandler implements GenericContainerScreenHandlerAccessor {

    @Mutable
    @Shadow @Final private Inventory inventory;

    @Shadow @Final private int rows;

    @Override
    public void setInventory(Inventory inventory, ServerPlayerEntity playerEntity) {
        this.inventory = inventory;
        ((ScreenHandler)(Object)this).slots.clear();

        inventory.onOpen(playerEntity);
        int i = (this.rows - 4) * 18;

        int n;
        int m;
        for(n = 0; n < this.rows; ++n) {
            for(m = 0; m < 9; ++m) {
                ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));
            }
        }

        for(n = 0; n < 3; ++n) {
            for(m = 0; m < 9; ++m) {
                ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(playerEntity.inventory, m + n * 9 + 9, 8 + m * 18, 103 + n * 18 + i));
            }
        }

        for(n = 0; n < 9; ++n) {
            ((ScreenHandlerAccessor)(Object)this).addSlot_public(new Slot(playerEntity.inventory, n, 8 + n * 18, 161 + i));
        }

        ((ScreenHandler)(Object)this).sendContentUpdates();
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
