package fr.catcore.gamegui.builder.gamebuilder.property.string;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.function.Function;

public class CancelStringEntry extends StringGuiEntry {

    private final Function<Void, NamedScreenHandlerFactory> callback;

    protected CancelStringEntry(Function<Void, NamedScreenHandlerFactory> callback) {
        super(new ItemStack(Items.RED_TERRACOTTA), new LiteralText("Cancel"));
        this.callback = callback;
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        player.openHandledScreen(this.callback.apply(null));
    }
}
