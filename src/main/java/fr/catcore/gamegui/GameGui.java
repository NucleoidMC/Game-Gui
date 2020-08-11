package fr.catcore.gamegui;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class GameGui implements ModInitializer {

    public static final Map<String, ItemConvertible> gameTypeItemConvertible;

    @Override
    public void onInitialize() {
        Reflection.initialize(GameGuiCustomItems.class);

    }

    static {
        Map<String, ItemConvertible> map = new HashMap<>();
        map.put("spleef:spleef", Items.IRON_SHOVEL);
        map.put("bedwars:bed_wars", Items.RED_BED);
        map.put("survivalgames:survivalgames", Items.IRON_SWORD);
        map.put("deacoudre:deacoudre", Items.WATER_BUCKET);
        map.put("loopdeloop:loopdeloop", Items.ELYTRA);
        map.put("farmy_feud:farmy_feud", Items.WHITE_WOOL);
        map.put("colorswap:color_swap", Items.JUKEBOX);
        map.put("electricfloor:electric_floor", Items.WHITE_STAINED_GLASS);

        gameTypeItemConvertible = new ImmutableMap.Builder<String, ItemConvertible>().putAll(map).build();
    }
}
