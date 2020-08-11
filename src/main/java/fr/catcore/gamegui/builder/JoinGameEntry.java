package fr.catcore.gamegui.builder;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.gegy1000.plasmid.game.ConfiguredGame;
import net.gegy1000.plasmid.game.GameType;
import net.gegy1000.plasmid.game.GameWorld;
import net.gegy1000.plasmid.game.GameWorldState;
import net.gegy1000.plasmid.game.config.GameConfigs;
import net.gegy1000.plasmid.game.player.JoinResult;
import net.gegy1000.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

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

    private JoinGameEntry withDimensionKey(RegistryKey<World> worldRegistryKey) {
        this.worldRegistryKey = worldRegistryKey;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ConfiguredGame<?> configuredGame = GameConfigs.get(this.gameConfigId);
        GameType<?> gameType = configuredGame.getType();
        this.icon.addLore(new LiteralText("Game config: " + this.gameConfigId.toString()));
        this.icon.addLore(new LiteralText("Game world: " + this.worldRegistryKey.getValue().toString()));
        ItemStack icon = this.icon.build().copy();
        Style style = Style.EMPTY.withItalic(false).withColor(Formatting.BLUE);
        icon.setCustomName(new LiteralText(gameType.getIdentifier().toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        MinecraftServer minecraftServer = player.getServer();
        PlayerManager playerManager = minecraftServer.getPlayerManager();
        ServerWorld serverWorld = minecraftServer.getWorld(this.worldRegistryKey);
        if (serverWorld == null) {
            throw new NullPointerException();
        }
        GameWorldState gameWorld = GameWorldState.forWorld(serverWorld);
        if (gameWorld == null) {
            throw new NullPointerException();
        }
        GameWorld openWorld = gameWorld.getOpenWorld();
        if (openWorld == null) {

        } else {
            JoinResult joinResult = openWorld.offerPlayer(player);
            if (joinResult.isErr()) {
                Text error = joinResult.getError();
            } else {
                Text joinMessage = player.getDisplayName().shallowCopy().append(" has joined the game lobby!").setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
                playerManager.broadcastChatMessage(joinMessage, MessageType.SYSTEM, Util.NIL_UUID);
            }
        }
    }
}
