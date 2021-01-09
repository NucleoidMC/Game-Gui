package fr.catcore.gamegui.builder.gamebuilder.property.string;

import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.inventory.codec.StringPropertyInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class CharEntry extends StringGuiEntry {
    protected CharEntry(String chr) {
        super(new ItemStack(Items.PAPER), new LiteralText(chr));
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
            if (inventory instanceof StringPropertyInventory) {
                ((StringPropertyInventory)inventory).addCharToCurrentlyEdited(this.getTitle().asString().charAt(0));
                ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(inventory, player);
            }
        }
    }
}
