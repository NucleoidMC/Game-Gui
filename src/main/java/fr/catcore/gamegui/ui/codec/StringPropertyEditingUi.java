package fr.catcore.gamegui.ui.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import fr.catcore.gamegui.inventory.codec.ConfigureGameMainInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class StringPropertyEditingUi implements NamedScreenHandlerFactory {
    private final Text title;
    private final Consumer<CodecGuiBuilder> builder;

    StringPropertyEditingUi(Text title, Consumer<CodecGuiBuilder> builder) {
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
        return new AnvilScreenHandler(syncId, playerInventory) {
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

            @Override
            protected boolean canTakeOutput(PlayerEntity player, boolean present) {
                return true;
            }
        };
    }
}
