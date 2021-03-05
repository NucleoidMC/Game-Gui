package fr.catcore.gamegui.builder.gamebuilder.property.string;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;

import java.util.function.Function;

public abstract class StringGuiEntry extends CodecGuiEntry {
    protected StringGuiEntry(ItemStack icon, Text title) {
        super(icon, title);
    }

    public static StringGuiEntry createEditedString() {
        return new EditedStringEntry();
    }

    public static CancelStringEntry createCancelString(Function<Void, NamedScreenHandlerFactory> callback) {
        return new CancelStringEntry(callback);
    }
}
