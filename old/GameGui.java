package fr.catcore.gamegui;

import com.google.common.reflect.Reflection;
import fr.catcore.gamegui.command.GameGuiCommand;
import fr.catcore.server.translations.api.ServerTranslations;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.config.GameConfigs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GameGui implements ModInitializer {
    private static final Map<String, Supplier<ItemStack>> gameTypeInfos = new HashMap<>();

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(GameGuiCustomItems.class);

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            GameGuiCommand.register(dispatcher);
        });
    }

    static {
        registerGameTypeIcon("spleef:spleef", () -> new ItemStack(Items.IRON_SHOVEL));
        registerGameTypeIcon("bedwars:bed_wars", () -> new ItemStack(Items.RED_BED));
        registerGameTypeIcon("survivalgames:survivalgames", () -> new ItemStack(Items.IRON_SWORD));
        registerGameTypeIcon("deacoudre:deacoudre", () -> new ItemStack(Items.WATER_BUCKET));
        registerGameTypeIcon("loopdeloop:loopdeloop", () -> new ItemStack(Items.ELYTRA));
        registerGameTypeIcon("farmy_feud:farmy_feud", () -> new ItemStack(Items.WHITE_WOOL));
        registerGameTypeIcon("colorswap:color_swap", () -> new ItemStack(Items.JUKEBOX));
        registerGameTypeIcon("electricfloor:electric_floor", () -> new ItemStack(Items.WHITE_STAINED_GLASS));
        registerGameTypeIcon("ascension:ascension", () -> new ItemStack(Items.RABBIT_FOOT));
        registerGameTypeIcon("withersweeper:withersweeper", () -> new ItemStack(Items.SOUL_SOIL));
        registerGameTypeIcon("shardthief:shard_thief", () -> new ItemStack(Items.PRISMARINE_SHARD));
        registerGameTypeIcon("murder_mystery:murder_mystery", () -> new ItemStack(Items.NETHERITE_SWORD));
        registerGameTypeIcon("wavedefense:wavedefense", () -> new ItemStack(Items.ZOMBIE_HEAD));
        registerGameTypeIcon("koth:koth", () -> new ItemStack(Items.LADDER));
        registerGameTypeIcon("clutchbridge:clutch_bridge", () -> new ItemStack(Items.MAGENTA_GLAZED_TERRACOTTA));
        registerGameTypeIcon("territorybattle:territory_battle", () -> new ItemStack(Items.GREEN_WOOL));
        registerGameTypeIcon("castlewars:castlewars", () -> new ItemStack(Items.VILLAGER_SPAWN_EGG));
        registerGameTypeIcon("capturetheflag:capturetheflag", () -> new ItemStack(Items.BLUE_BANNER));
        registerGameTypeIcon("tnttag:tnttag", () -> new ItemStack(Items.TNT));
        registerGameTypeIcon("minefield:minefield", () -> new ItemStack(Items.TRIPWIRE_HOOK));
        registerGameTypeIcon("codebreaker:codebreaker", () -> new ItemStack(Items.REDSTONE_TORCH));
        registerGameTypeIcon("cabinfever:cabinfever", () -> new ItemStack(Items.COAL));
        registerGameTypeIcon("buildbattle:buildbattle", () -> new ItemStack(Items.CRAFTING_TABLE));
        registerGameTypeIcon("totemhunt:totemhunt", () -> new ItemStack(Items.TOTEM_OF_UNDYING));
        registerGameTypeIcon("downpour:downpour", () -> new ItemStack(Items.POTION));
        registerGameTypeIcon("creeperfall:creeperfall", () -> new ItemStack(Items.CREEPER_HEAD));
        registerGameTypeIcon("cakewars:cakewars", () -> new ItemStack(Items.CAKE));
        registerGameTypeIcon("snowballfight:snowballfight", () -> new ItemStack(Items.SNOWBALL));
        registerGameTypeIcon("beaconbreakers:beaconbreakers", () -> new ItemStack(Items.BEACON));
        registerGameTypeIcon("werewolf:werewolf", () -> new ItemStack(Items.BONE));
        registerGameTypeIcon("parkourrun:parkour_run", () -> new ItemStack(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
        registerGameTypeIcon("elytron:elytron", () -> new ItemStack(Items.ELYTRA)); // Broken elytra
        registerGameTypeIcon("bowbattle:bowbattle", () -> new ItemStack(Items.BOW));
        registerGameTypeIcon("microbattle:microbattle", () -> new ItemStack(Items.DIAMOND_SWORD));
        registerGameTypeIcon("anvildrop:anvil_drop", () -> new ItemStack(Items.ANVIL));
        registerGameTypeIcon("duels:duels", () -> new ItemStack(Items.DIAMOND_AXE));
        registerGameTypeIcon("skywars:skywars", () -> new ItemStack(Items.ENDER_PEARL));
        registerGameTypeIcon("cornmaze:cornmaze", () -> new ItemStack(Items.HAY_BLOCK));
        registerGameTypeIcon("irritaterrun:irritaterrun", () -> new ItemStack(Items.BAKED_POTATO));
        registerGameTypeIcon("vacuole:vacuole", () -> new ItemStack(Items.RED_WOOL));
        registerGameTypeIcon("destroythemonument:destroythemonument", () -> new ItemStack(Items.PRISMARINE_BRICKS));
        registerGameTypeIcon("siege:siege", () -> new ItemStack(Items.SHIELD));
        registerGameTypeIcon("mineout:mineout", () -> new ItemStack(Items.DIAMOND_PICKAXE));
        registerGameTypeIcon("caverncrawler:caverncrawler", () -> new ItemStack(Items.COBWEB));
        registerGameTypeIcon("creative_party:creative_party", () -> new ItemStack(Items.COMMAND_BLOCK));
        registerGameTypeIcon("fortress:fortress", () -> new ItemStack(Items.BLUE_WOOL));
        registerGameTypeIcon("quakecraft:quakecraft", () -> new ItemStack(Items.IRON_HOE));
        registerGameTypeIcon("castle_wars:castle_wars", () -> new ItemStack(Items.STONE_BRICK_WALL));
    }

    public static void registerGameTypeIcon(Identifier gameTypeId, Supplier<ItemStack> icon) {
        registerGameTypeIcon(gameTypeId.toString(), icon);
    }

    public static void registerGameTypeIcon(String gameTypeId, Supplier<ItemStack> icon) {
        if (gameTypeInfos.containsKey(gameTypeId)) {
            LOGGER.warn("Tried to register two icons for game type " + gameTypeId + ", Current:" + gameTypeInfos.get(gameTypeId).get().toString() + "; New:" + icon.get().toString());
        } else {
            gameTypeInfos.put(gameTypeId, icon);
        }
    }

    public static Supplier<ItemStack> getGameInfos(String id) {
        return gameTypeInfos.getOrDefault(id, () -> new ItemStack(Items.BARRIER));
    }

    public static Supplier<ItemStack> getGameInfos(Identifier id) {
        return getGameInfos(id.toString());
    }

    public static Text[] getGameTypeDescription(Identifier gameTypeID) {
        List<Text> lines = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            if (ServerTranslations.INSTANCE.getLanguage("en_us")
                    .remote.contains(String.format("game.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i))) {
                lines.add(new TranslatableText(String.format("game.%s.%s.desc.%s", gameTypeID.getNamespace(), gameTypeID.getPath(), i)).formatted(Formatting.ITALIC, Formatting.DARK_PURPLE));
            }
        }
        return lines.toArray(new Text[0]);
    }

    public static ConfiguredGame<?>[] getConfigsFromType(GameType<?> type) {
        List<ConfiguredGame<?>> configs = new ArrayList<>();
        for (Identifier id : GameConfigs.getKeys()) {
            ConfiguredGame<?> game = GameConfigs.get(id);
            if (game.getType() == type) {
                configs.add(game);
            }
        }
        return configs.toArray(new ConfiguredGame[0]);
    }

    public static boolean hasConfigsForType(GameType<?> type) {
        for (Identifier id : GameConfigs.getKeys()) {
            ConfiguredGame<?> game = GameConfigs.get(id);
            if (game.getType() == type) {
                return true;
            }
        }
        return false;
    }
}
