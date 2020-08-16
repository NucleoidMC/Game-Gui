package fr.catcore.gamegui.builder;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.catcore.gamegui.GameGui;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.Plasmid;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class OpenConfiguredGameEntry {
    private final ItemStackBuilder icon;
    private Identifier gameType;
    private Identifier gameConfig;

    private OpenConfiguredGameEntry(ItemStack icon) {
        this.icon = ItemStackBuilder.of(icon);
    }

    public static OpenConfiguredGameEntry ofItem(ItemStack icon) {
        return new OpenConfiguredGameEntry(icon);
    }

    public static OpenConfiguredGameEntry ofItem(ItemConvertible icon) {
        return new OpenConfiguredGameEntry(new ItemStack(icon));
    }

    public OpenConfiguredGameEntry withGameConfig(Identifier gameConfig) {
        this.gameConfig = gameConfig;
        return this;
    }

    public OpenConfiguredGameEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStack icon = this.icon.build().copy();
        Style style = Style.EMPTY.withItalic(false).withColor(Formatting.BLUE);
        icon.setCustomName(new LiteralText(gameConfig.toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        ConfiguredGame<?> game = GameConfigs.get(this.gameConfig);
        if (game == null) return;
        PlayerManager playerManager = server.getPlayerManager();
        LiteralText announcement = new LiteralText("Game is opening! Hold tight..");
        playerManager.broadcastChatMessage(announcement.formatted(Formatting.GRAY), MessageType.SYSTEM, Util.NIL_UUID);
        server.submit(() -> {
            try {
                game.open(server).handle((v, throwable) -> {
                    if (throwable == null) {
                        String command = "/game join";
                        ClickEvent joinClick = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
                        HoverEvent joinHover = new HoverEvent(net.minecraft.text.HoverEvent.Action.SHOW_TEXT, new LiteralText(command));
                        Style joinStyle = Style.EMPTY.withFormatting(Formatting.UNDERLINE).withColor(Formatting.BLUE).withClickEvent(joinClick).withHoverEvent(joinHover);
                        Text openMessage = (new LiteralText("Game has opened! ")).append((new LiteralText("Click here to join")).setStyle(joinStyle));
                        playerManager.broadcastChatMessage(openMessage, MessageType.SYSTEM, Util.NIL_UUID);
                        GameGui.LOGGER.info(player.getName().toString() + " opened game: " + this.gameConfig);
                        Collection<GameWorld> games = GameWorld.getOpen();
                        GameWorld gameWorld = (GameWorld)games.stream().findFirst().orElse((GameWorld) (Object)null);
                        if (gameWorld != null) {
                            CompletableFuture<JoinResult> resultFuture = CompletableFuture.supplyAsync(() -> {
                                return gameWorld.offerPlayer(player);
                            }, server);
                            resultFuture.thenAccept((joinResult) -> {
                                if (joinResult.isErr()) {
                                    player.closeHandledScreen();
                                    Text error = joinResult.getError();
                                    player.sendMessage(error.shallowCopy().formatted(Formatting.RED), false);
                                } else {
                                    Text joinMessage = player.getDisplayName().shallowCopy().append(" has joined the game lobby!").formatted(Formatting.YELLOW);
                                    Iterator var5 = gameWorld.getPlayers().iterator();

                                    while(var5.hasNext()) {
                                        ServerPlayerEntity otherPlayer = (ServerPlayerEntity)var5.next();
                                        otherPlayer.sendMessage(joinMessage, false);
                                    }

                                }
                            });
                        }
                    } else {
                        player.closeHandledScreen();
                        Plasmid.LOGGER.error("Failed to start game", throwable);
                        Object message;
                        if (throwable instanceof GameOpenException) {
                            message = ((GameOpenException)throwable).getReason().shallowCopy();
                        } else {
                            message = new LiteralText("The game threw an unexpected error while starting!");
                        }

                        playerManager.broadcastChatMessage(((MutableText)message).formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID);
                    }

                    return null;
                });
            } catch (Throwable throwable) {
                player.closeHandledScreen();
                Plasmid.LOGGER.error("Failed to start game", throwable);
                Object message;
                if (throwable instanceof GameOpenException) {
                    message = ((GameOpenException)throwable).getReason().shallowCopy();
                } else {
                    message = new LiteralText("The game threw an unexpected error while starting!");
                }

                playerManager.broadcastChatMessage(((MutableText)message).formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID);
            }
        });
    }
}
