package fr.catcore.gamegui.builder.gamebuilder.main;

import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.codec.GameTypeCoderNBTRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;

public class ConfigGuiEntry extends MainGuiEntry {
    protected ConfigGuiEntry() {
        super(new ItemStack(Items.CRAFTING_TABLE), new LiteralText("Game Config Options"));
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        String gameType = GameCreatorHelper.getPlayerCurrentConfig(player.getUuid()).getString("type");

        CompoundTag gameTag = GameTypeCoderNBTRegistry.getCodecNBT(GameType.get(new Identifier(gameType)));

        for (String key : gameTag.getKeys()) {
            CompoundTag tag = gameTag.getCompound(key);
            String fieldType = tag.getString("type");
            boolean optional = tag.getBoolean("optional");
            System.out.println("Field Name: " + key + ", Type: " + fieldType + ", Optional: " + optional);

            if (fieldType.equals("object")) {
                tag = tag.getCompound("object_type");

                for (String key1 : tag.getKeys()) {
                    CompoundTag tag1 = tag.getCompound(key1);
                    fieldType = tag1.getString("type");
                    optional = tag1.getBoolean("optional");
                    if (fieldType.equals("resource_key")) {
                        System.out.println("    Field Name: " + key1 + ", Type: " + fieldType + ", Registry: " + tag1.getString("registry") + ", Optional: " + optional);
                    } else {
                        System.out.println("    Field Name: " + key1 + ", Type: " + fieldType + ", Optional: " + optional);
                    }
                }
            }
        }
    }
}
