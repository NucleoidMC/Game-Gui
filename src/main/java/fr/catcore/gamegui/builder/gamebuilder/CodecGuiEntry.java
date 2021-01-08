package fr.catcore.gamegui.builder.gamebuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public abstract class CodecGuiEntry {
    private final ItemStack icon;
    private final Text title;

    protected CodecGuiEntry(ItemStack icon, Text title) {
        this.icon = icon;
        this.title = title;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Text getTitle() {
        return title;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        return ItemStackBuilder.of(this.icon).setName(this.title).build();
    }

    public abstract void onClick(ServerPlayerEntity player);
}
