package fr.catcore.gamegui.builder.gamebuilder.main;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public abstract class MainGuiEntry extends CodecGuiEntry {
    protected MainGuiEntry(ItemStack icon, Text title) {
        super(icon, title);
    }

    public static MainGuiEntry createConfig() {
        return new ConfigGuiEntry();
    }

    public static MainGuiEntry createName() {
        return new NameGuiEntry();
    }
    public static MainGuiEntry createType() {
        return new TypeGuiEntry();
    }
    public static MainGuiEntry createLaunch() {
        return new LaunchGuiEntry();
    }
}
