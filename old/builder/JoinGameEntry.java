package fr.catcore.gamegui.builder;

import fr.catcore.gamegui.item.JoinGameItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class JoinGameEntry extends GuiEntry {
    private final Type type;

    public JoinGameEntry(Type type) {
        super(type.icon);
        this.type = type;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(new TranslatableText(this.type.translationKey));
        switch (this.type) {
            case JOIN_CHANNEL:
                builder.addLore(new LiteralText("Not implemented yet").formatted(Formatting.RED));
            case JOIN_OPENED:
        }
        return builder.build();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        switch (this.type) {
            case JOIN_OPENED:
                JoinGameItem.openJoinOpenedScreen(player);
                break;
            case JOIN_CHANNEL:

        }
    }

    public enum Type {
        JOIN_OPENED("text.game_gui.gui.join.opened", new ItemStack(Items.WRITABLE_BOOK)),
        JOIN_CHANNEL("text.game_gui.gui.join.channel", new ItemStack(Items.WRITTEN_BOOK));

        private final String translationKey;
        private final ItemStack icon;

        Type(String translationKey, ItemStack icon) {
            this.translationKey = translationKey;
            this.icon = icon;
        }
    }
}
