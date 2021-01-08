package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.Plasmid;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.channel.GameChannel;
import xyz.nucleoid.plasmid.game.channel.GameChannelManager;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class OpenConfiguredGameEntry extends GuiEntry {
    private Identifier gameConfig;

    protected OpenConfiguredGameEntry(ItemStack icon) {
        super(icon);
    }

    public OpenConfiguredGameEntry withGameConfig(Identifier gameConfig) {
        this.gameConfig = gameConfig;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(GameGui.getGameConfigName(this.gameConfig));
        for (Text text : GameGui.getGameConfigDescription(this.gameConfig)) {
            builder.addLore(text);
        }
        return builder.build().copy();
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        ConfiguredGame<?> game = GameConfigs.get(this.gameConfig);
        if (game == null) return;
        PlayerManager playerManager = server.getPlayerManager();
        server.submit(() -> {

            if (player != null) {
                ManagedGameSpace currentGameSpace = ManagedGameSpace.forWorld(player.world);
                if (currentGameSpace != null) {
                    currentGameSpace.removePlayer(player);
                }
            }


            GameChannelManager channelManager = GameChannelManager.get(server);
            try {
                channelManager.openOneshot(this.gameConfig, game).handle((channel, throwable) -> {
                    if (throwable == null) {
                        joinGame(channel, player);
                        onOpenSuccess(channel, player, this.gameConfig, game, playerManager);
                    } else {
                        onOpenError(playerManager, throwable);
                    }

                    return null;
                });
            } catch (Throwable var5) {
                onOpenError(playerManager, var5);
            }
        });
    }

    private static void onOpenSuccess(GameChannel channel, ServerPlayerEntity playerEntity, Identifier gameId,ConfiguredGame<?> game, PlayerManager playerManager) {
        Text openMessage = new TranslatableText("text.plasmid.game.open.opened", playerEntity.getDisplayName(), new LiteralText(game.getDisplayName(gameId))).append(channel.createJoinLink());
        playerManager.broadcastChatMessage(openMessage, MessageType.SYSTEM, Util.NIL_UUID);
    }

    private static void onOpenError(PlayerManager playerManager, Throwable throwable) {
        Plasmid.LOGGER.error("Failed to start game", throwable);
        Object message;
        if (throwable instanceof GameOpenException) {
            message = ((GameOpenException)throwable).getReason().shallowCopy();
        } else {
            message = new TranslatableText("text.plasmid.game.open.error");
        }

        playerManager.broadcastChatMessage(((MutableText)message).formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID);
    }

    private static void joinGame(GameChannel channel, ServerPlayerEntity player) {
        if (player != null && ManagedGameSpace.forWorld(player.world) == null) {
            channel.requestJoin(player);
        }
    }
}
