package fr.catcore.gamegui;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import fr.catcore.gamegui.command.GameGuiCommand;
import fr.catcore.gamegui.util.GameInfos;
import fr.catcore.server.translations.api.resource.language.ServerLanguageManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
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

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            GameGuiCommand.register(dispatcher);
        });
    }

    static {
        Map<String, GameInfos> map = new HashMap<>();
        map.put("spleef:spleef", new GameInfos(Items.IRON_SHOVEL));
        map.put("bedwars:bed_wars", new GameInfos(Items.RED_BED));
        map.put("survivalgames:survivalgames", new GameInfos(Items.IRON_SWORD));
        map.put("deacoudre:deacoudre", new GameInfos(Items.WATER_BUCKET));
        map.put("loopdeloop:loopdeloop", new GameInfos(Items.ELYTRA));
        map.put("farmy_feud:farmy_feud", new GameInfos(Items.WHITE_WOOL));
        map.put("colorswap:color_swap", new GameInfos(Items.JUKEBOX));
        map.put("electricfloor:electric_floor", new GameInfos(Items.WHITE_STAINED_GLASS));
        map.put("ascension:ascension", new GameInfos(Items.RABBIT_FOOT));
        map.put("withersweeper:withersweeper", new GameInfos(Items.SOUL_SOIL));
        map.put("shardthief:shard_thief", new GameInfos(Items.PRISMARINE_SHARD));
        map.put("murder_mystery:murder_mystery", new GameInfos(Items.NETHERITE_SWORD));
        map.put("wavedefense:wavedefense", new GameInfos(Items.ZOMBIE_HEAD));
        map.put("koth:koth", new GameInfos(Items.LADDER));
        map.put("clutchbridge:clutch_bridge", new GameInfos(Items.MAGENTA_GLAZED_TERRACOTTA));
        map.put("territorybattle:territory_battle", new GameInfos(Items.GREEN_WOOL));
        map.put("castlewars:castlewars", new GameInfos(Items.VILLAGER_SPAWN_EGG));
        map.put("capturetheflag:capturetheflag", new GameInfos(Items.BLUE_BANNER));

        gameTypeInfos = new ImmutableMap.Builder<String, GameInfos>().putAll(map).build();
    }

    public static GameInfos getGameInfos(String id) {
        return gameTypeInfos.getOrDefault(id, new GameInfos(Items.BARRIER));
    }

    public static Text getGameTypeName(Identifier gameTypeID) {
        if (ServerLanguageManager.INSTANCE.getLanguage("en_us")
                .hasTranslation(String.format("gameType.%s.%s", gameTypeID.getNamespace(), gameTypeID.getPath()))) {
            return new TranslatableText(String.format("gameType.%s.%s", gameTypeID.getNamespace(), gameTypeID.getPath()));
        }
        return new LiteralText(gameTypeID.toString());
    }

    public static Text[] getGameTypeDescription(Identifier gameTypeID) {
        List<Text> lines = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            if (ServerLanguageManager.INSTANCE.getLanguage("en_us")
                    .hasTranslation(String.format("gameType.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i))) {
                lines.add(new TranslatableText(String.format("gameType.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i)).formatted(Formatting.ITALIC, Formatting.DARK_PURPLE));
            }
        }
        return lines.toArray(new Text[0]);
    }

    public static Text getGameConfigName(Identifier gameTypeID) {
        if (ServerLanguageManager.INSTANCE.getLanguage("en_us")
                .hasTranslation(String.format("gameConfig.%s.%s", gameTypeID.getNamespace(), gameTypeID.getPath()))) {
            return new TranslatableText(String.format("gameConfig.%s.%s", gameTypeID.getNamespace(), gameTypeID.getPath()));
        }
        ConfiguredGame<?> configuredGame = GameConfigs.get(gameTypeID);
        if (configuredGame == null) return new LiteralText(gameTypeID.toString());
        return new LiteralText(configuredGame.getName());
    }

    public static Text[] getGameConfigDescription(Identifier gameTypeID) {
        List<Text> lines = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            if (ServerLanguageManager.INSTANCE.getLanguage("en_us")
                    .hasTranslation(String.format("gameConfig.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i))) {
                lines.add(new TranslatableText(String.format("gameConfig.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i)).formatted(Formatting.ITALIC, Formatting.DARK_PURPLE));
            }
        }
        return lines.toArray(new Text[0]);
    }

    public static GameInfos getGameInfos(Identifier identifier) {
        return getGameInfos(identifier.toString());
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
