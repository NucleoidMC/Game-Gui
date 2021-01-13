package fr.catcore.gamegui.builder.gamebuilder.main;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class LaunchGuiEntry extends MainGuiEntry{
    protected LaunchGuiEntry() {
        super(new ItemStack(Items.FIREWORK_ROCKET), new LiteralText("Launch Custom Config"));
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
