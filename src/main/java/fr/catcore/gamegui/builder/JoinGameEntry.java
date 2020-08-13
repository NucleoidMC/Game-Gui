package fr.catcore.gamegui.builder;

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
    private final ItemStackBuilder icon;
    private Identifier gameConfigId;
    private RegistryKey<World> worldRegistryKey;

    private JoinGameEntry(ItemStack icon) {
        this.icon = ItemStackBuilder.of(icon);
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
        this.icon.addLore(new LiteralText("Game config: " + this.gameConfigId.toString()));
        this.icon.addLore(new LiteralText("Game type: " + gameType.getIdentifier().toString()));
        ItemStack icon = this.icon.build().copy();
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
            CompletableFuture<JoinResult> resultFuture = CompletableFuture.supplyAsync(() -> {
                return openWorld.offerPlayer(player);
            }, minecraftServer);
            resultFuture.thenAccept((joinResult) -> {
                if (joinResult.isErr()) {
                    player.closeHandledScreen();
                    Text error = joinResult.getError();
                    player.sendMessage(error.shallowCopy().formatted(Formatting.RED), false);
                } else {
                    Text joinMessage = player.getDisplayName().shallowCopy().append(" has joined the game lobby!").formatted(Formatting.YELLOW);
                    Iterator var5 = openWorld.getPlayers().iterator();

                    while(var5.hasNext()) {
                        ServerPlayerEntity otherPlayer = (ServerPlayerEntity)var5.next();
                        otherPlayer.sendMessage(joinMessage, false);
                    }

                }
            });
        }
    }
}
