package fr.catcore.gamegui;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        map.put("ascension:ascension", Items.RABBIT_FOOT);
        map.put("withersweeper:withersweeper", Items.SOUL_SOIL);
        map.put("shardthief:shard_thief", Items.PRISMARINE_SHARD);
        map.put("murder_mystery:murder_mystery", Items.NETHERITE_SWORD);
        map.put("wavedefense:wavedefense", Items.ZOMBIE_HEAD);
        map.put("koth:koth", Items.LADDER);
        map.put("clutchbridge:clutch_bridge", Items.MAGENTA_GLAZED_TERRACOTTA);
        map.put("territorybattle:territory_battle", Items.GREEN_WOOL);
        map.put("castlewars:castlewars", Items.VILLAGER_SPAWN_EGG);

        gameTypeItemConvertible = new ImmutableMap.Builder<String, ItemConvertible>().putAll(map).build();
    }

    public static Identifier[] getConfigsFromType(Identifier identifier) {
        List<Identifier> configs = new ArrayList<>();
        for (Identifier configuredGameID : GameConfigs.getKeys()) {
            ConfiguredGame configuredGame = GameConfigs.get(configuredGameID);
            if (configuredGame.getType().getIdentifier() == identifier) configs.add(configuredGameID);
        }
        return configs.toArray(new Identifier[0]);
    }
}
