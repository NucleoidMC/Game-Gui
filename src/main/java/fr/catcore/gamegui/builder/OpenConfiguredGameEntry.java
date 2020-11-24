package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.server.translations.api.LocalizableText;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.Plasmid;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class OpenConfiguredGameEntry {
    private final ItemStack icon;
    private Identifier gameConfig;

    private OpenConfiguredGameEntry(ItemStack icon) {
        this.icon = icon;
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

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.icon);
        builder.setName(LocalizableText.asLocalizedFor(GameGui.getGameConfigName(this.gameConfig), (LocalizationTarget) player));
        for (Text text : GameGui.getGameConfigDescription(this.gameConfig)) {
            builder.addLore(LocalizableText.asLocalizedFor(text, (LocalizationTarget) player));
        }
        return builder.build().copy();
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer server = player.getServer();
        ConfiguredGame<?> game = GameConfigs.get(this.gameConfig);
        if (game == null) return;
        PlayerManager playerManager = server.getPlayerManager();
        server.submit(() -> {
            try {
                game.open(server).handle((v, throwable) -> {
                    if (throwable == null) {
                        onOpenSuccess(player, this.gameConfig, playerManager);
                        joinGame(v, player);
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

    private static void onOpenSuccess(ServerPlayerEntity playerEntity, Identifier gameId, PlayerManager playerManager) {
        String command = "/game join " + gameId;
        ClickEvent joinClick = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        HoverEvent joinHover = new HoverEvent(net.minecraft.text.HoverEvent.Action.SHOW_TEXT, new LiteralText(command));
        Style joinStyle = Style.EMPTY.withFormatting(Formatting.UNDERLINE).withColor(Formatting.BLUE).withClickEvent(joinClick).withHoverEvent(joinHover);
        Text openMessage = new TranslatableText("text.plasmid.game.open.opened", playerEntity.getDisplayName(), gameId).append(new TranslatableText("text.plasmid.game.open.join").setStyle(joinStyle));
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

    private static void joinGame(GameSpace gameWorld, ServerPlayerEntity playerEntity) {
        ManagedGameSpace managedGameSpace = ManagedGameSpace.forWorld(gameWorld.getWorld());
        if (managedGameSpace == null) {
            playerEntity.closeHandledScreen();
        } else {
            managedGameSpace.offerPlayer(playerEntity).thenAccept((joinResult) -> {
                if (joinResult.isError()) {
                    Text error = joinResult.getError();
                    playerEntity.sendMessage(error.shallowCopy().formatted(Formatting.RED), MessageType.CHAT, Util.NIL_UUID);
                }

            });
        }
    }
}
