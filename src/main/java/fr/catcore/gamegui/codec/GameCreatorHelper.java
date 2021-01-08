package fr.catcore.gamegui.codec;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;

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

    public static CompoundTag getPlayerCurrentConfig(UUID playerUUID) {
        return CURRENT_EDITED_CONFIG.getOrDefault(playerUUID, new CompoundTag());
    }

    public static void clearPlayerCurrentConfig(UUID playerUUID) {
        CURRENT_EDITED_CONFIG.remove(playerUUID);
    }
}
