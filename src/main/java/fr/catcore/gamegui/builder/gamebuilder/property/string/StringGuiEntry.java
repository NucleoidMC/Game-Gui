package fr.catcore.gamegui.builder.gamebuilder.property.string;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public abstract class StringGuiEntry extends CodecGuiEntry {
    protected StringGuiEntry(ItemStack icon, Text title) {
        super(icon, title);
    }

    public static StringGuiEntry createEditedString() {
        return new EditedStringEntry();
    }

    public static StringGuiEntry createCharEntry(String chr) {
        return new CharEntry(chr);
    }

    public static StringGuiEntry createDone() {
        return new DoneStringEntry();
    }
}
