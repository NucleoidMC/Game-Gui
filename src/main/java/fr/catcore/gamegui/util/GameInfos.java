package fr.catcore.gamegui.util;

import fr.catcore.gamegui.GameGui;
import net.minecraft.item.ItemConvertible;

import java.util.Map;

public class GameInfos {

    private final ItemConvertible icon;
    private final String name;

    public GameInfos(String name, ItemConvertible icon) {
        this.name = name;
        this.icon = icon;
    }

    public ItemConvertible getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        for (Map.Entry<String, GameInfos> entry : GameGui.gameInfos()) {
            if (entry.getValue() == this) return entry.getKey();
        }
        return "minecraft:null";
    }
}
