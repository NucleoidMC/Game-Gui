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

public class JoinGameEntry extends GuiEntry{
    private Identifier gameConfigId;
    private ManagedGameSpace gameSpace;

    protected JoinGameEntry(ItemStack icon) {
        super(icon);
    }

    public JoinGameEntry withGameConfig(Identifier gameConfigId) {
        this.gameConfigId = gameConfigId;
        return this;
    }

    public JoinGameEntry withGameSpace(ManagedGameSpace gameSpace) {
        this.gameSpace = gameSpace;
        return this;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ConfiguredGame<?> configuredGame = GameConfigs.get(this.gameConfigId);
        if (configuredGame == null) return ItemStack.EMPTY;
        GameType<?> gameType = configuredGame.getType();

        Text configName = GameGui.getGameConfigName(this.gameConfigId);
        Text typeName = new TranslatableText("text.game_gui.join.type", GameGui.getGameTypeName(gameType.getIdentifier()));

        ItemStackBuilder iconBuilder = ItemStackBuilder.of(this.getIcon());
        iconBuilder.setName(configName);
        iconBuilder.addLore(typeName);

        ManagedGameSpace gameSpace = this.gameSpace;
        if (gameSpace == null) {
            if (!(player.currentScreenHandler == null || player.currentScreenHandler == player.playerScreenHandler)) {
                player.closeHandledScreen();
                JoinGameItem.openJoinScreen(player);
            }
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
