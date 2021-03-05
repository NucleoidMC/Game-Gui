package fr.catcore.gamegui.builder.gamebuilder.property.main;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PropertyEntry extends MainGuiEntry{
    protected PropertyEntry(ItemStack icon, Text title) {
        super(icon, title);
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
