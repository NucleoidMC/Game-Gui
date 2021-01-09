package fr.catcore.gamegui.builder.gamebuilder;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class EmptyGuiEntry extends CodecGuiEntry {
    public EmptyGuiEntry() {
        super(new ItemStack(Items.AIR), new LiteralText(""));
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        return ItemStackBuilder.of(this.getIcon()).setName(this.getTitle()).build();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }
}
