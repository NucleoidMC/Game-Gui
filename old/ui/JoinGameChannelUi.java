package fr.catcore.gamegui.ui;

import fr.catcore.gamegui.builder.JoinOpenedGameBuilder;
import fr.catcore.gamegui.inventory.JoinOpenedGameInventory;
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

public class JoinGameChannelUi implements NamedScreenHandlerFactory {
    private final Text title;
    private final Consumer<JoinOpenedGameBuilder> builder;

    JoinGameChannelUi(Text title, Consumer<JoinOpenedGameBuilder> builder) {
        this.title = title;
        this.builder = builder;
    }

    public static JoinGameChannelUi create(Text title, Consumer<JoinOpenedGameBuilder> builder) {
        return new JoinGameChannelUi(title, builder);
    }

    public Text getDisplayName() {
        return this.title;
    }

    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        final ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        JoinOpenedGameInventory inventory = new JoinOpenedGameInventory(serverPlayer, this.builder);
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, syncId, playerInventory, inventory, 6) {
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
            public boolean isNotRestricted(PlayerEntity player) {
                return true;
            }
        };
    }
}
