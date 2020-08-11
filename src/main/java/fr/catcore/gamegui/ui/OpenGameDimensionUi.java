package fr.catcore.gamegui.ui;

import fr.catcore.gamegui.builder.OpenGameDimensionBuilder;
import fr.catcore.gamegui.inventory.OpenGameDimensionInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class OpenGameDimensionUi implements NamedScreenHandlerFactory {
    private final Text title;
    private final Consumer<OpenGameDimensionBuilder> builder;

    OpenGameDimensionUi(Text title, Consumer<OpenGameDimensionBuilder> builder) {
        this.title = title;
        this.builder = builder;
    }

    public static OpenGameDimensionUi create(Text title, Consumer<OpenGameDimensionBuilder> builder) {
        return new OpenGameDimensionUi(title, builder);
    }

    public Text getDisplayName() {
        return this.title;
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        OpenGameDimensionInventory inventory = new OpenGameDimensionInventory(serverPlayer, this.builder);
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X5, syncId, playerInventory, inventory, 5) {
            public ItemStack transferSlot(PlayerEntity player, int invSlot) {
                this.resendInventory();
                return ItemStack.EMPTY;
            }

            public ItemStack onSlotClick(int slot, int data, SlotActionType action, PlayerEntity player) {
                if (action != SlotActionType.SWAP && action != SlotActionType.THROW && action != SlotActionType.CLONE) {
                    return super.onSlotClick(slot, data, action, player);
                } else {
                    this.resendInventory();
                    return ItemStack.EMPTY;
                }
            }

            private void resendInventory() {
                serverPlayer.onHandlerRegistered(this, this.getStacks());
            }
        };
    }
}
