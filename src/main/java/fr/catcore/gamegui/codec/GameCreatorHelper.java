package fr.catcore.gamegui.codec;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;

public class GameCreatorHelper {

    private static final Object2ObjectMap<UUID, CompoundTag> CURRENT_EDITED_CONFIG = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<UUID, String> CURRENT_EDITED_FIELD = new Object2ObjectOpenHashMap<>();

    public static void startCreatingConfig(UUID playerUUID, CompoundTag configTag) {
        CURRENT_EDITED_CONFIG.put(playerUUID, configTag);
        CURRENT_EDITED_FIELD.put(playerUUID, "");
    }

    public static void setEditingField(UUID playerUUID, String fieldName) {
        CURRENT_EDITED_FIELD.replace(playerUUID, fieldName);
    }

    public static String getEditingField(UUID playerUUID) {
        return CURRENT_EDITED_FIELD.get(playerUUID);
    }

    public static Tag getEditingFieldValue(UUID playerUUID) {
        CompoundTag compoundTag = CURRENT_EDITED_CONFIG.get(playerUUID);
        String fieldId = CURRENT_EDITED_FIELD.get(playerUUID);
        String[] parts = fieldId.split("/");
        Tag result = null;
        for (String part : parts) {
            result = compoundTag.get(part);
            if (result instanceof CompoundTag) {
                compoundTag = (CompoundTag) result;
            } else {
                break;
            }
        }


        return result;
    }

    public static void setEditingFieldValue(UUID playerUUID, Tag value) {
        CompoundTag compoundTag = CURRENT_EDITED_CONFIG.get(playerUUID);
        String fieldId = CURRENT_EDITED_FIELD.get(playerUUID);
        String[] parts = fieldId.split("/");
        Tag result = null;
        for (String part : parts) {
            result = compoundTag.get(part);
            if (result instanceof CompoundTag) {
                compoundTag = (CompoundTag) result;
            } else {
                compoundTag.put(part, value);
            }
        }
    }

    public static CompoundTag getPlayerCurrentConfig(UUID playerUUID) {
        return CURRENT_EDITED_CONFIG.getOrDefault(playerUUID, new CompoundTag());
    }

    public static void clearPlayerCurrentConfig(UUID playerUUID) {
        CURRENT_EDITED_CONFIG.remove(playerUUID);
    }
}
