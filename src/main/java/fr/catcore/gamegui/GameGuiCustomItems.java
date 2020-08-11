package fr.catcore.gamegui;

import fr.catcore.gamegui.builder.JoinGameEntry;
import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import fr.catcore.gamegui.ui.JoinGameUi;
import fr.catcore.gamegui.ui.OpenGameTypeUi;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.GameWorldState;
import xyz.nucleoid.plasmid.item.CustomItem;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

public class GameGuiCustomItems {
    public static final CustomItem JOIN_GAME = CustomItem.builder()
            .id(new Identifier("game_gui", "join_game"))
            .name(new LiteralText("Join Game"))
            .onUse((playerEntity, world, hand) -> {
                playerEntity.openHandledScreen(JoinGameUi.create(new LiteralText("Join Game"), joinGameBuilder -> {
                    for (ServerWorld serverWorld : playerEntity.getServer().getWorlds()) {
                        GameWorldState gameWorldState = GameWorldState.forWorld(serverWorld);
                        if (gameWorldState == null) continue;
                        if (!gameWorldState.isOpen()) continue;
                        GameWorld gameWorld = gameWorldState.getOpenWorld();
                        if (gameWorld == null) continue;
                        joinGameBuilder.add(JoinGameEntry.ofItem(Items.CONDUIT).withDimensionKey(serverWorld.getRegistryKey()));
                    }
                }));
                return TypedActionResult.fail(null);
            })
            .register();
    public static final CustomItem OPEN_GAME = CustomItem.builder()
            .id(new Identifier("game_gui", "open_game"))
            .name(new LiteralText("Open Game"))
            .onUse((playerEntity, world, hand) -> {
                playerEntity.openHandledScreen(OpenGameTypeUi.create(new LiteralText("Open Game"), openGameTypeBuilder -> {
                    for (Identifier gameType : GameType.REGISTRY.keySet()) {
                        openGameTypeBuilder.add(OpenGameTypeEntry.ofItem(GameGui.gameTypeItemConvertible.getOrDefault(gameType.toString(), Items.BARRIER)).withGameType(gameType));
                    }
                }));
                return TypedActionResult.fail(null);
            })
            .register();
}
