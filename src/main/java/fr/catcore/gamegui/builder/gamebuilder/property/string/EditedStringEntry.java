package fr.catcore.gamegui.builder.gamebuilder.property.string;

import fr.catcore.gamegui.accessor.ForgingScreenHandlerAccessor;
import fr.catcore.gamegui.inventory.codec.StringPropertyInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class EditedStringEntry extends StringGuiEntry {
    protected EditedStringEntry() {
        super(new ItemStack(Items.NAME_TAG), new LiteralText("Value"));
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        if (player.currentScreenHandler instanceof ForgingScreenHandlerAccessor) {
            Inventory inventory = ((ForgingScreenHandlerAccessor)player.currentScreenHandler).getInventory();
            if (inventory instanceof StringPropertyInventory) {
                String currentlyEdited = ((StringPropertyInventory)inventory).getCurrentlyEdited();
                return ItemStackBuilder.of(super.createIcon(player)).setName(new LiteralText(currentlyEdited)).build();
            }
        }

        return super.createIcon(player);
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
