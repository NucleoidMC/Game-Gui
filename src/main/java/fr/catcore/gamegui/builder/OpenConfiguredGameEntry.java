package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.ui.OpenGameDimensionUi;
import xyz.nucleoid.plasmid.game.GameWorldState;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class OpenConfiguredGameEntry {
    private final ItemStackBuilder icon;
    private Identifier gameType;
    private Identifier gameConfig;

    private OpenConfiguredGameEntry(ItemStack icon) {
        this.icon = ItemStackBuilder.of(icon);
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

    public OpenConfiguredGameEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStack icon = this.icon.build().copy();
        Style style = Style.EMPTY.withItalic(false).withColor(Formatting.BLUE);
        icon.setCustomName(new LiteralText(gameConfig.toString()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        player.openHandledScreen(OpenGameDimensionUi.create(new LiteralText("Open Game"), openGameDimensionBuilder -> {
            for (ServerWorld serverWorld : player.getServer().getWorlds()) {
                if (serverWorld == null) continue;
                GameWorldState gameWorld = GameWorldState.forWorld(serverWorld);
                if (gameWorld == null) continue;
                if (gameWorld.isOpen()) continue;
                openGameDimensionBuilder.add(OpenGameDimensionEntry.ofItem(Items.CONDUIT)
                        .withGameConfig(this.gameConfig)
                        .withGameType(this.gameType)
                        .withWorld(serverWorld.getRegistryKey())
                );
            }
        }));
    }
}
