package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameConfigMetadata;
import fr.catcore.gamegui.item.JoinGameItem;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class JoinGameEntry extends GuiEntry{
    private final GameConfigMetadata config;
    private ManagedGameSpace gameSpace;

    protected JoinGameEntry(GameConfigMetadata config) {
        super(config.getIconOr(new ItemStack(Blocks.BARRIER)));
        this.config = config;
    }

    public JoinGameEntry withGameSpace(ManagedGameSpace gameSpace) {
        this.gameSpace = gameSpace;
        return this;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        GameType<?> gameType = this.config.getGame().getType();

        Text configName =this.config.getName();
        Text typeName = new TranslatableText("text.game_gui.join.type", gameType.getName());

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

    @Override
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
