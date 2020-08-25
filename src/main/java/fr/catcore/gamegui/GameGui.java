package fr.catcore.gamegui;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import fr.catcore.gamegui.util.GameInfos;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.*;

public class GameGui implements ModInitializer {

    private static final Map<String, GameInfos> gameTypeInfos;

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(GameGuiCustomItems.class);

    }

    static {
        Map<String, GameInfos> map = new HashMap<>();
        map.put("spleef:spleef", new GameInfos("Spleef", Items.IRON_SHOVEL));
        map.put("bedwars:bed_wars", new GameInfos("Bed Wars", Items.RED_BED));
        map.put("survivalgames:survivalgames", new GameInfos("Survival Games", Items.IRON_SWORD));
        map.put("deacoudre:deacoudre", new GameInfos("Dé à Coudre", Items.WATER_BUCKET));
        map.put("loopdeloop:loopdeloop", new GameInfos("Loop-de-Loop", Items.ELYTRA));
        map.put("farmy_feud:farmy_feud", new GameInfos("Farmy Feud", Items.WHITE_WOOL));
        map.put("colorswap:color_swap", new GameInfos("Color Swap", Items.JUKEBOX));
        map.put("electricfloor:electric_floor", new GameInfos("Electric Floor", Items.WHITE_STAINED_GLASS));
        map.put("ascension:ascension", new GameInfos("Ascension", Items.RABBIT_FOOT));
        map.put("withersweeper:withersweeper", new GameInfos("Withersweeper", Items.SOUL_SOIL));
        map.put("shardthief:shard_thief", new GameInfos("Shard Thief", Items.PRISMARINE_SHARD));
        map.put("murder_mystery:murder_mystery", new GameInfos("Murder Mystery", Items.NETHERITE_SWORD));
        map.put("wavedefense:wavedefense", new GameInfos("Wave Defense", Items.ZOMBIE_HEAD));
        map.put("koth:koth", new GameInfos("King of the Hill", Items.LADDER));
        map.put("clutchbridge:clutch_bridge", new GameInfos("Cluth Bridge", Items.MAGENTA_GLAZED_TERRACOTTA));
        map.put("territorybattle:territory_battle", new GameInfos("Territory Battle", Items.GREEN_WOOL));
        map.put("castlewars:castlewars", new GameInfos("Castle Wars", Items.VILLAGER_SPAWN_EGG));
        map.put("capturetheflag:capturetheflag", new GameInfos("Capture the Flag", Items.BLUE_BANNER));

        gameTypeInfos = new ImmutableMap.Builder<String, GameInfos>().putAll(map).build();
    }

    public static GameInfos getGameInfos(String id) {
        return gameTypeInfos.getOrDefault(id, new GameInfos(id, Items.BARRIER));
    }

    public static GameInfos getGameInfos(Identifier identifier) {
        return getGameInfos(identifier.toString());
    }

    public static Set<Map.Entry<String, GameInfos>> gameInfos() {
        return gameTypeInfos.entrySet();
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
