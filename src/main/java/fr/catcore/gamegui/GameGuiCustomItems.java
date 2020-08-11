package fr.catcore.gamegui;

import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import fr.catcore.gamegui.ui.JoinGameUi;
import fr.catcore.gamegui.ui.OpenGameTypeUi;
import net.gegy1000.plasmid.game.GameType;
import net.gegy1000.plasmid.game.GameWorld;
import net.gegy1000.plasmid.game.GameWorldState;
import net.gegy1000.plasmid.item.CustomItem;
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
                        GameWorld gameWorld = gameWorldState.getOpenWorld();
                        if (gameWorld == null) continue;

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
