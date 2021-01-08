package fr.catcore.gamegui.builder.gamebuilder.main;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class ConfigGuiEntry extends MainGuiEntry {
    protected ConfigGuiEntry() {
        super(new ItemStack(Items.CRAFTING_TABLE), new LiteralText("Game Config Options"));
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
