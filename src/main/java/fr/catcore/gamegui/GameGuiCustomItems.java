package fr.catcore.gamegui;

import fr.catcore.gamegui.item.JoinGameItem;
import fr.catcore.gamegui.item.OpenGameItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;

public class GameGuiCustomItems {
    public static final Item JOIN_GAME = register("join_game", new JoinGameItem(new Item.Settings().maxCount(1)));
    public static final Item OPEN_GAME = register("open_game", new OpenGameItem(new Item.Settings().maxCount(1)));

    private static Item register(String identifier, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("game_gui", identifier), item);
    }
}
