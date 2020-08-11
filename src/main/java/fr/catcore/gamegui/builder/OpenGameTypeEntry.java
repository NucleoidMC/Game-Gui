package fr.catcore.gamegui.builder;

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

public class OpenGameTypeEntry {
    private final ItemStackBuilder icon;
    private Identifier gameType;

    private OpenGameTypeEntry(ItemStack icon) {
        this.icon = ItemStackBuilder.of(icon);
    }

    public static OpenGameTypeEntry ofItem(ItemStack icon) {
        return new OpenGameTypeEntry(icon);
    }

    public static OpenGameTypeEntry ofItem(ItemConvertible icon) {
        return new OpenGameTypeEntry(new ItemStack(icon));
    }

    public OpenGameTypeEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStack icon = this.icon.build().copy();
        Style style = Style.EMPTY.withItalic(false).withColor(Formatting.BLUE);
        icon.setCustomName(new LiteralText(gameType.toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {

    }
}
