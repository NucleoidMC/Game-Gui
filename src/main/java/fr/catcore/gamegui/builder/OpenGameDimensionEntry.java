package fr.catcore.gamegui.builder;

import xyz.nucleoid.plasmid.Plasmid;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.GameWorldState;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class OpenGameDimensionEntry {
    private final ItemStackBuilder icon;
    private Identifier gameType;
    private Identifier gameConfig;
    private RegistryKey<World> worldRegistryKey;

    private OpenGameDimensionEntry(ItemStack icon) {
        this.icon = ItemStackBuilder.of(icon);
    }

    public static OpenGameDimensionEntry ofItem(ItemStack icon) {
        return new OpenGameDimensionEntry(icon);
    }

    public static OpenGameDimensionEntry ofItem(ItemConvertible icon) {
        return new OpenGameDimensionEntry(new ItemStack(icon));
    }

    public OpenGameDimensionEntry withGameConfig(Identifier gameConfig) {
        this.gameConfig = gameConfig;
        return this;
    }

    public OpenGameDimensionEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public OpenGameDimensionEntry withWorld(RegistryKey<World> worldRegistryKey) {
        this.worldRegistryKey = worldRegistryKey;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStack icon = this.icon.build().copy();
        Style style = Style.EMPTY.withItalic(false).withColor(Formatting.BLUE);
        icon.setCustomName(new LiteralText(worldRegistryKey.getValue().toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer minecraftServer = player.getServer();
        ServerWorld serverWorld = minecraftServer.getWorld(this.worldRegistryKey);
        if (serverWorld == null) return;
        GameWorldState gameWorld = GameWorldState.forWorld(serverWorld);
        if (gameWorld == null) return;
        ConfiguredGame<?> game = GameConfigs.get(this.gameConfig);
        PlayerManager playerManager = minecraftServer.getPlayerManager();
        LiteralText announcement = new LiteralText("Game is opening! Hold tight..");
        playerManager.broadcastChatMessage(announcement.formatted(Formatting.GRAY), MessageType.SYSTEM, Util.NIL_UUID);
        game.open(gameWorld).handle((v, throwable) -> {
            if (throwable == null) {
                String command = "/game join " + this.worldRegistryKey.getValue();
                ClickEvent joinClick = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
                HoverEvent joinHover = new HoverEvent(net.minecraft.text.HoverEvent.Action.SHOW_TEXT, new LiteralText(command));
                Style joinStyle = Style.EMPTY.withFormatting(Formatting.UNDERLINE).withColor(Formatting.BLUE).withClickEvent(joinClick).setHoverEvent(joinHover);
                Text openMessage = (new LiteralText("Game has opened! ")).append((new LiteralText("Click here to join")).setStyle(joinStyle));
                playerManager.broadcastChatMessage(openMessage, MessageType.SYSTEM, Util.NIL_UUID);
                GameWorld gameWorld1 = gameWorld.getOpenWorld();
                gameWorld1.offerPlayer(player);
            } else {
                player.closeHandledScreen();
                Plasmid.LOGGER.error("Failed to start game", throwable);
                playerManager.broadcastChatMessage(new LiteralText("An exception occurred while trying to start game"), MessageType.SYSTEM, Util.NIL_UUID);
            }

            return null;
        });
    }
}
