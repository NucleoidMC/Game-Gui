package fr.catcore.gamegui.builder.gamebuilder.property.main;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class PropertyEntry extends MainGuiEntry {

    private String name;
    private String type;
    private boolean optional;

    protected PropertyEntry(String name, String type, boolean optional) {
        super(new ItemStack(getIconForType(type)), new LiteralText(name));
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(super.createIcon(player));

        builder.addLore(new TranslatableText("text.game_gui.codec.type", new TranslatableText(getTranslationKeyForType(this.type))));

        if (!this.optional) {
            builder.addLore(new TranslatableText("text.game_gui.codec.mandatory"));
        }

        return builder.build();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {

    }

    private static String getTranslationKeyForType(String type) {
        return "text.game_gui.codec.type." + type;
    }

    private static Item getIconForType(String type) {
        Item item = Items.BARRIER;
        switch (type) {

            default:
        }
        return item;
    }
}
