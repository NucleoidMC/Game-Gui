package fr.catcore.gamegui.builder.gamebuilder.property.main;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public abstract class MainGuiEntry extends CodecGuiEntry {
    protected MainGuiEntry(ItemStack icon, Text title) {
        super(icon, title);
    }
}
