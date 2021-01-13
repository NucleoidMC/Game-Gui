package fr.catcore.gamegui.builder.gamebuilder.main;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class TypeGuiEntry extends PropertyMainGuiEntry {
    protected TypeGuiEntry() {
        super(new ItemStack(Items.NAME_TAG), new LiteralText("Game Type"), "type");
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
