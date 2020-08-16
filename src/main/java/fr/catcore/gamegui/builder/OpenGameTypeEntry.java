package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.ui.OpenConfiguredGameUi;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
        icon.setCustomName(new LiteralText(GameGui.getGameInfos(this.gameType).getName()));
        return icon;
    }

    public void onClick(ServerPlayerEntity player) {
        player.openHandledScreen(OpenConfiguredGameUi.create(new LiteralText("Open Game"), openConfiguredGameBuilder -> {
            Identifier[] configs = GameGui.getConfigsFromType(this.gameType);
            for (Identifier configuredGame : configs) {
                openConfiguredGameBuilder.add(OpenConfiguredGameEntry
                        .ofItem(GameGui.getGameInfos(this.gameType).getIcon())
                        .withGameType(this.gameType)
                        .withGameConfig(configuredGame));
            }
        }));
    }
}
