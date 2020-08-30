package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.ui.OpenConfiguredGameUi;
import fr.catcore.server.translations.api.LocalizableText;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
    private final ItemStack icon;
    private Identifier gameType;

    private OpenGameTypeEntry(ItemStack icon) {
        this.icon = icon;
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
        ItemStackBuilder builder = ItemStackBuilder.of(this.icon);
        builder.setName(LocalizableText.asLocalizedFor(GameGui.getGameTypeName(this.gameType), (LocalizationTarget) player));
        for (Text text : GameGui.getGameTypeDescription(this.gameType)) {
            builder.addLore(LocalizableText.asLocalizedFor(text, (LocalizationTarget) player));
        }
        return builder.build().copy();
    }

    public void onClick(ServerPlayerEntity player) {
        player.openHandledScreen(OpenConfiguredGameUi.create(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.gui.open"), (LocalizationTarget) player),
                openConfiguredGameBuilder -> {
            Identifier[] configs = GameGui.getConfigsFromType(this.gameType);
            for (Identifier configuredGame : configs) {
                openConfiguredGameBuilder.add(OpenConfiguredGameEntry
                        .ofItem(GameGui.getGameInfos(this.gameType).getIcon())
                        .withGameConfig(configuredGame));
            }
        }));
    }
}
