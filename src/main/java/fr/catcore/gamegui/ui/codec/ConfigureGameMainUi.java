package fr.catcore.gamegui.ui.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.inventory.codec.ConfigureGameMainInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ConfigureGameMainUi implements NamedScreenHandlerFactory {
    private final Text title;
    private final Consumer<CodecGuiBuilder> builder;

    ConfigureGameMainUi(Text title, Consumer<CodecGuiBuilder> builder) {
        this.title = title;
        this.builder = builder;
    }

    public static ConfigureGameMainUi create(Text title, Consumer<CodecGuiBuilder> builder) {
        return new ConfigureGameMainUi(title, builder);
    }

    public Text getDisplayName() {
        return this.title;
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ConfigureGameMainInventory inventory = new ConfigureGameMainInventory(serverPlayer, this.builder);
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inventory, 6) {
            public ItemStack transferSlot(PlayerEntity player, int invSlot) {
                this.resendInventory();
                return ItemStack.EMPTY;
            }

            public ItemStack onSlotClick(int slot, int data, SlotActionType action, PlayerEntity player) {
                if (action == SlotActionType.SWAP || action == SlotActionType.THROW || action == SlotActionType.CLONE) {
                    this.resendInventory();
                    return ItemStack.EMPTY;
                }

                return super.onSlotClick(slot, data, action, player);
            }

            private void resendInventory() {
                serverPlayer.onHandlerRegistered(this, this.getStacks());
            }

            @Override
            public boolean isNotRestricted(PlayerEntity player) {
                return true;
            }
        };
    }
}
