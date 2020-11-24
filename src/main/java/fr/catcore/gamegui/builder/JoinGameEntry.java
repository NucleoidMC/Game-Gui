package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.item.JoinGameItem;
import fr.catcore.server.translations.api.LocalizableText;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.text.TranslatableText;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

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
        iconBuilder.addLore(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.join.config", GameGui.getGameConfigName(this.gameConfigId)), (LocalizationTarget) player));
        iconBuilder.addLore(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.join.type", GameGui.getGameTypeName(gameType.getIdentifier())), (LocalizationTarget) player));

        MinecraftServer minecraftServer = player.getServer();
        ServerWorld serverWorld = minecraftServer.getWorld(this.worldRegistryKey);
        if (serverWorld == null) {
            player.closeHandledScreen();
            JoinGameItem.openJoinScreen(player);
        }
        ManagedGameSpace openWorld = ManagedGameSpace.forWorld(serverWorld);
        if (openWorld == null) {
            player.closeHandledScreen();
            JoinGameItem.openJoinScreen(player);
        }
        int playerCount = 0;
        try {
            playerCount = openWorld.getPlayerCount();
        } catch (NullPointerException e) {}
        iconBuilder.addLore(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.join.player_count", playerCount), (LocalizationTarget) player));

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
        ManagedGameSpace openWorld = ManagedGameSpace.forWorld(serverWorld);
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
