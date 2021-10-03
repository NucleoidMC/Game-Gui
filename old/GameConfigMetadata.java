package fr.catcore.gamegui;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import fr.catcore.server.translations.api.LocalizationTarget;
import fr.catcore.server.translations.api.resource.language.ServerLanguage;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.plasmid.game.ConfiguredGame;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class GameConfigMetadata {
    private static final Identifier ICON_KEY = new Identifier("gamegui", "icon");
    private static final Identifier DESCRIPTION_KEY = new Identifier("gamegui", "description");

    private static final int DESCRIPTION_WRAP_LENGTH = 40;

    private static final Codec<ItemStack> ITEM_CODEC = Codec.either(ItemStack.CODEC, Registry.ITEM)
            .xmap(either -> either.map(Function.identity(), ItemStack::new), Either::left);

    private final ConfiguredGame<?> game;
    private final ItemStack icon;
    private final String descriptionKey;

    private GameConfigMetadata(ConfiguredGame<?> game, ItemStack icon, String descriptionKey) {
        this.game = game;
        this.icon = icon;
        this.descriptionKey = descriptionKey;
    }

    public static GameConfigMetadata parse(ConfiguredGame<?> game) {
        ItemStack icon = game.getCustomValue(ITEM_CODEC, ICON_KEY).orElse(null);
        String descriptionKey = game.getCustomValue(Codec.STRING, DESCRIPTION_KEY).orElse(null);
        return new GameConfigMetadata(game, icon, descriptionKey);
    }

    public ConfiguredGame<?> getGame() {
        return this.game;
    }

    public Text getName() {
        return this.game.getNameText();
    }

    @Nullable
    public ItemStack getIcon() {
        return this.icon;
    }

    @NotNull
    public ItemStack getIconOr(ItemStack stack) {
        return this.icon != null ? this.icon : stack;
    }

    @Nullable
    public Text[] getDescriptionFor(LocalizationTarget target) {
        if (this.descriptionKey == null) {
            return null;
        }

        ServerLanguage language = target.getLanguage();
        String description = language.remote.getOrNull(this.descriptionKey);
        if (description == null) {
            return null;
        }

        String[] words = description.split(" ");

        List<Text> lines = new ArrayList<>(description.length() / DESCRIPTION_WRAP_LENGTH);

        StringBuilder lineBuilder = new StringBuilder(DESCRIPTION_WRAP_LENGTH);
        for (String word : words) {
            if (lineBuilder.length() + word.length() > DESCRIPTION_WRAP_LENGTH) {
                lines.add(new LiteralText(lineBuilder.toString()));
                lineBuilder.setLength(0);
            }

            if (lineBuilder.length() > 0) {
                lineBuilder.append(" ");
            }
            lineBuilder.append(word);
        }

        if (lineBuilder.length() > 0) {
            lines.add(new LiteralText(lineBuilder.toString()));
        }

        return lines.toArray(new Text[0]);
    }
}
