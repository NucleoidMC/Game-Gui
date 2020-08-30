package fr.catcore.gamegui.util;

import net.minecraft.item.ItemConvertible;

public class GameInfos {

    private final ItemConvertible icon;

    public GameInfos(ItemConvertible icon) {
        this.icon = icon;
    }

    public ItemConvertible getIcon() {
        return icon;
    }
}
