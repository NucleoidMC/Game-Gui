package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.concurrent.CompletableFuture;

public class JoinGameEntry {
    private final ItemStack icon;
    private Identifier gameConfigId;
    private RegistryKey<World> worldRegistryKey;

    private JoinGameEntry(ItemStack icon) {
        this.icon = icon;
    }

    public static JoinGameEntry ofItem(ItemStack icon) {
        return new JoinGameEntry(icon);
    }

    public static JoinGameEntry ofItem(ItemConvertible icon) {
        return new JoinGameEntry(new ItemStack(icon));
    }

    public JoinGameEntry withGameConfig(Identifier gameConfigId) {
        this.gameConfigId = gameConfigId;
        return this;
    }

    public JoinGameEntry withDimensionKey(RegistryKey<World> worldRegistryKey) {
        this.worldRegistryKey = worldRegistryKey;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ConfiguredGame<?> configuredGame = GameConfigs.get(this.gameConfigId);
        GameType<?> gameType = configuredGame.getType();
        ItemStackBuilder iconBuilder = ItemStackBuilder.of(this.icon);
        iconBuilder.addLore(new LiteralText("Game config: " + this.gameConfigId.toString()));
        iconBuilder.addLore(new LiteralText("Game type: " + GameGui.getGameInfos(gameType.getIdentifier()).getName()));

        MinecraftServer minecraftServer = player.getServer();
        ServerWorld serverWorld = minecraftServer.getWorld(this.worldRegistryKey);
        if (serverWorld == null) {
            throw new NullPointerException();
        }
        GameWorld openWorld = GameWorld.forWorld(serverWorld);
        if (openWorld == null) {
            player.closeHandledScreen();
        }
        int playerCount = openWorld.getPlayerCount();
        iconBuilder.addLore(new LiteralText("Player count: " + playerCount));

        ItemStack icon = iconBuilder.build().copy();
        icon.setCustomName(new LiteralText(this.worldRegistryKey.getValue().toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer minecraftServer = player.getServer();
        ServerWorld serverWorld = minecraftServer.getWorld(this.worldRegistryKey);
        if (serverWorld == null) {
            throw new NullPointerException();
        }
        GameWorld openWorld = GameWorld.forWorld(serverWorld);
        if (openWorld == null) {
            player.closeHandledScreen();
        } else {
            openWorld.offerPlayer(player).thenAccept((joinResult) -> {
                if (joinResult.isError()) {
                    Text error = joinResult.getError();
                    player.sendMessage(error.shallowCopy().formatted(Formatting.RED), MessageType.CHAT, Util.NIL_UUID);
                }

            });
        }
    }
}
