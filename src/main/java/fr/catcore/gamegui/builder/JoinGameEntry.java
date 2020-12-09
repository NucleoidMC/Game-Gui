package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.item.JoinGameItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.config.GameConfigs;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class JoinGameEntry {
    private final ItemStack icon;
    private Identifier gameConfigId;
    private ManagedGameSpace gameSpace;

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

    public JoinGameEntry withGameSpace(ManagedGameSpace gameSpace) {
        this.gameSpace = gameSpace;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ConfiguredGame<?> configuredGame = GameConfigs.get(this.gameConfigId);
        GameType<?> gameType = configuredGame.getType();

        Text configName = GameGui.getGameConfigName(this.gameConfigId);
        Text typeName = new TranslatableText("text.game_gui.join.type", GameGui.getGameTypeName(gameType.getIdentifier()));

        ItemStackBuilder iconBuilder = ItemStackBuilder.of(this.icon);
        iconBuilder.setName(configName);
        iconBuilder.addLore(typeName);

        ManagedGameSpace gameSpace = this.gameSpace;
        if (gameSpace == null) {
            player.closeHandledScreen();
            JoinGameItem.openJoinScreen(player);
            return ItemStack.EMPTY;
        }

        iconBuilder.addLore(new TranslatableText("text.game_gui.join.player_count", gameSpace.getPlayerCount()));

        return iconBuilder.build();
    }

    public void onClick(ServerPlayerEntity player) {
        ManagedGameSpace gameSpace = this.gameSpace;
        if (gameSpace == null) {
            player.closeHandledScreen();
            return;
        }

        gameSpace.offerPlayer(player).thenAccept((joinResult) -> {
            if (joinResult.isError()) {
                Text error = joinResult.getError();
                player.sendMessage(error.shallowCopy().formatted(Formatting.RED), MessageType.CHAT, Util.NIL_UUID);
            }
        });
    }
}
