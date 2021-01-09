package fr.catcore.gamegui.inventory.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.builder.gamebuilder.property.string.StringGuiEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StringPropertyInventory extends CodecGuiInventory<StringGuiEntry> {

    private String currentlyEdited;

    public StringPropertyInventory(String currentlyEdited, ServerPlayerEntity player, Consumer<CodecGuiBuilder> builder) {
        super(player, builder);
        this.currentlyEdited = currentlyEdited;
    }

    public void addCharToCurrentlyEdited(char chr) {
        this.currentlyEdited += chr;
    }

    public String getCurrentlyEdited() {
        return currentlyEdited;
    }

    @Override
    public List<Integer> getFillableSlots() {
        List<Integer> list = new ArrayList<>();

        list.add(0);

        return list;
    }

    @Override
    public int size() {
        return 2;
    }
}
