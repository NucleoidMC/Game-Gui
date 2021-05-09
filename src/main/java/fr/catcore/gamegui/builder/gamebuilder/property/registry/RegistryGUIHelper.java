package fr.catcore.gamegui.builder.gamebuilder.property.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class RegistryGUIHelper {

    public static <T, R extends Registry<T>> Map<Identifier, Text> create(RegistryKey<R> registryKey) {
        Registry<T> registry = (Registry<T>) Registry.REGISTRIES.get(registryKey.getValue());

        assert registry != null;
        Map<Identifier, Text> map = new HashMap<>();
        for (Map.Entry<RegistryKey<T>, T> entry : registry.getEntries()) {
            RegistryKey<T> registryKey1 = entry.getKey();
            T value = entry.getValue();

            if (value instanceof Block) {
                Block valueBlock = (Block) value;
                MutableText name = valueBlock.getName();
                Item item;
                if (name.toString().startsWith("block.")) {
                    name = new LiteralText(registryKey1.getValue().toString());
                    item = Items.BARRIER;
                } else {
                    item = valueBlock.asItem();
                    if (item == Items.AIR) item = Items.BARRIER;
                }
                valueBlock.getStateManager().getProperties();
                map.put(registryKey1.getValue(), name);
            } else if (value instanceof Item) {
                Item valueItem = (Item) value;
                Text name = valueItem.getName();
                if (name.toString().startsWith("item.")) {
                    name = new LiteralText(registryKey1.getValue().toString());
                }
                if (valueItem == Items.AIR) valueItem = Items.BARRIER;
                map.put(registryKey1.getValue(), name);
            }

            map.put(registryKey1.getValue(), new LiteralText(registryKey1.getValue().toString()));
        }
        return map;
    }
}
