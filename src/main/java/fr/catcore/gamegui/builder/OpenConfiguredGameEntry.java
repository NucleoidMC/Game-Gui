package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameConfigMetadata;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.Plasmid;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.channel.GameChannel;
import xyz.nucleoid.plasmid.game.channel.GameChannelManager;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class OpenConfiguredGameEntry extends GuiEntry {
    private final GameConfigMetadata config;

    public OpenConfiguredGameEntry(GameConfigMetadata config) {
        super(config.getIconOr(new ItemStack(Items.BARRIER)));
        this.config = config;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(this.config.getName());

        Text[] description = this.config.getDescriptionFor((LocalizationTarget) player);
        if (description != null) {
            for (Text text : description) {
                builder.addLore(text);
            }
        }

        return builder.build();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        PlayerManager playerManager = server.getPlayerManager();
        server.submit(() -> {
            ManagedGameSpace currentGameSpace = ManagedGameSpace.forWorld(player.world);
            if (currentGameSpace != null) {
                currentGameSpace.removePlayer(player);
            }

            GameChannelManager channelManager = GameChannelManager.get(server);
            try {
                ConfiguredGame<?> game = this.config.getGame();
                channelManager.openOneshot(game.getType().getIdentifier(), game).handle((channel, throwable) -> {
                    if (throwable == null) {
                        joinGame(channel, player);
                        onOpenSuccess(channel, player, game, playerManager);
                    } else {
                        onOpenError(playerManager, throwable);
                    }

                    return null;
                });
            } catch (Throwable t) {
                onOpenError(playerManager, t);
            }
        });
    }

    private static void onOpenSuccess(GameChannel channel, ServerPlayerEntity playerEntity, ConfiguredGame<?> game, PlayerManager playerManager) {
        Text openMessage = new TranslatableText("text.plasmid.game.open.opened", playerEntity.getDisplayName(), game.getNameText().shallowCopy().formatted(Formatting.GRAY)
                .append(channel.createJoinLink()));
        playerManager.broadcastChatMessage(openMessage, MessageType.SYSTEM, Util.NIL_UUID);
    }

    private static void onOpenError(PlayerManager playerManager, Throwable throwable) {
        Plasmid.LOGGER.error("Failed to start game", throwable);

        GameOpenException gameOpenException = GameOpenException.unwrap(throwable);

        MutableText message;
        if (gameOpenException != null) {
            message = ((GameOpenException) throwable).getReason().shallowCopy();
        } else {
            message = new TranslatableText("text.plasmid.game.open.error");
        }

        playerManager.broadcastChatMessage(message.formatted(Formatting.RED), MessageType.SYSTEM, Util.NIL_UUID);
    }

    private static void joinGame(GameChannel channel, ServerPlayerEntity player) {
        if (player != null && ManagedGameSpace.forWorld(player.world) == null) {
            channel.requestJoin(player);
        }
    }
}
