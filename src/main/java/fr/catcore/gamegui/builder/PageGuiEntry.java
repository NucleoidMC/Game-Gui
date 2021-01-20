package fr.catcore.gamegui.builder;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PageGuiEntry extends GuiEntry {


    public PageGuiEntry(ItemStack icon) {
        super(icon);
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        return this.getIcon();
    }
}
