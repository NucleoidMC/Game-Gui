package fr.catcore.gamegui.inventory.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigureGameMainInventory extends CodecGuiInventory<MainGuiEntry> {
    public ConfigureGameMainInventory(ServerPlayerEntity player, Consumer<CodecGuiBuilder> builder) {
        super(player, builder);
    }

    @Override
    public List<Integer> getFillableSlots() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(7);
        return list;
    }
}
