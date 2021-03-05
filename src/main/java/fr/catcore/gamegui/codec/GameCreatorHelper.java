package fr.catcore.gamegui.codec;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.*;

import java.util.*;

public class GameCreatorHelper {

    private static final Object2ObjectMap<UUID, CompoundTag> CURRENT_EDITED_CONFIG = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<UUID, Map<String, Boolean>> PATH_TO_CURRENTLY_EDITED = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<UUID, String> CURRENTLY_EDITED_FIELD = new Object2ObjectOpenHashMap<>();

    public static void startCreatingConfig(UUID playerUUID, CompoundTag configTag) {
        CURRENT_EDITED_CONFIG.put(playerUUID, configTag);
        PATH_TO_CURRENTLY_EDITED.put(playerUUID, new HashMap<>());
        CURRENTLY_EDITED_FIELD.put(playerUUID, null);
    }

    public static CompoundTag getPlayerCurrentConfig(UUID playerUUID) {
        return CURRENT_EDITED_CONFIG.getOrDefault(playerUUID, new CompoundTag());
    }

    public static void setCurrentlyEditedPath(UUID playerUUID, Map<String, Boolean> path, String fieldName) {
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).clear();
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).putAll(path);
        CURRENTLY_EDITED_FIELD.put(playerUUID, fieldName);
    }

    public static void setCurrentlyEditedPath(UUID playerUUID, Map<String, Boolean> path) {
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).clear();
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).putAll(path);
        CURRENTLY_EDITED_FIELD.put(playerUUID, null);
    }

    public static void setCurrentlyEditedPath(UUID playerUUID, String name, Boolean type) {
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).clear();
        PATH_TO_CURRENTLY_EDITED.get(playerUUID).put(name, type);
        CURRENTLY_EDITED_FIELD.put(playerUUID, null);
    }

    public static Map<String, Boolean> getCurrentlyEditedPath(UUID playerUUID) {
        return PATH_TO_CURRENTLY_EDITED.get(playerUUID);
    }

    public static String getEditedFieldName(UUID playerUUID) {
        return CURRENTLY_EDITED_FIELD.get(playerUUID);
    }

    public static Tag getEditedFieldValue(UUID playerUUID) {
        Tag value = null;

        CompoundTag rootTag = CURRENT_EDITED_CONFIG.get(playerUUID);

        String currentField = CURRENTLY_EDITED_FIELD.get(playerUUID);

        CompoundTag currentCompound = rootTag;
        ListTag currentList = null;

        // true -> ListTag
        // false -> CompoundTag
        boolean type = false;

        for (Map.Entry<String, Boolean> entry : PATH_TO_CURRENTLY_EDITED.get(playerUUID).entrySet()) {
            boolean eType = entry.getValue();
            String eName = entry.getKey();

            if (type) {
                if (eType) {
                    if (currentList == null) {
                        System.out.println("CONCERN THIS SHOULD NOT HAPPEN");
                        return null;
                    } else {
                        currentList = currentList.getList(Integer.parseInt(eName));
                    }
                } else {
                    if (currentList == null) {
                        System.out.println("CONCERN THIS SHOULD NOT HAPPEN");
                        return null;
                    } else {
                        currentCompound = currentList.getCompound(Integer.parseInt(eName));
                    }
                }
            } else {
                if (eType) {
                    if (currentCompound == null) {
                        currentList = (ListTag) rootTag.get(eName);
                    } else {
                        currentList = (ListTag) currentCompound.get(eName);
                    }
                } else {
                    if (currentCompound == null) {
                        currentCompound = rootTag.getCompound(eName);
                    } else {
                        currentCompound = currentCompound.getCompound(eName);
                    }
                }
            }

            type = eType;
        }

        if (type) {
            value = currentList.get(Integer.parseInt(currentField));
        } else {
            value = currentCompound.get(currentField);
        }

        return value;
    }

    public static void setCurrentlyEditedValue(UUID playerUUID, Object value) {
        CompoundTag rootTag = CURRENT_EDITED_CONFIG.get(playerUUID);

        String currentField = CURRENTLY_EDITED_FIELD.get(playerUUID);

        CompoundTag currentCompound = rootTag;
        ListTag currentList = null;

        // true -> ListTag
        // false -> CompoundTag
        boolean type = false;

        for (Map.Entry<String, Boolean> entry : PATH_TO_CURRENTLY_EDITED.get(playerUUID).entrySet()) {
            boolean eType = entry.getValue();
            String eName = entry.getKey();

            if (type) {
                if (eType) {
                    if (currentList == null) {
                        System.out.println("CONCERN THIS SHOULD NOT HAPPEN");
                        return;
                    } else {
                        currentList = currentList.getList(Integer.parseInt(eName));
                    }
                } else {
                    if (currentList == null) {
                        System.out.println("CONCERN THIS SHOULD NOT HAPPEN");
                        return;
                    } else {
                        currentCompound = currentList.getCompound(Integer.parseInt(eName));
                    }
                }
            } else {
                if (eType) {
                    if (currentCompound == null) {
                        currentList = (ListTag) rootTag.get(eName);
                    } else {
                        currentList = (ListTag) currentCompound.get(eName);
                    }
                } else {
                    if (currentCompound == null) {
                        currentCompound = rootTag.getCompound(eName);
                    } else {
                        currentCompound = currentCompound.getCompound(eName);
                    }
                }
            }

            type = eType;
        }

        if (type) { // true -> ListTag
            Tag tag = new CompoundTag();

            if (value instanceof Tag) {
                tag = (Tag) value;
            } else if (value instanceof String) {
                tag = StringTag.of((String) value);
            } else if (value instanceof Integer) {
                tag = IntTag.of((Integer) value);
            } else if (value instanceof Long) {
                tag = LongTag.of((Long) value);
            } else if (value instanceof Float) {
                tag = FloatTag.of((Float) value);
            } else if (value instanceof Boolean) {
                tag = ByteTag.of((Boolean) value);
            } else if (value instanceof Double) {
                tag = DoubleTag.of((Double) value);
            } else if (value instanceof UUID) {
                tag = NbtHelper.fromUuid((UUID) value);
            } else if (value instanceof Short) {
                tag = ShortTag.of((Short) value);
            } else if (value instanceof Byte) {
                tag = ByteTag.of((Byte) value);
            } else if (value instanceof int[]) {
                tag = new IntArrayTag((int[]) value);
            } else if (value instanceof byte[]) {
                tag = new ByteArrayTag((byte[]) value);
            } else if (value instanceof long[]) {
                tag = new LongArrayTag((long[]) value);
            } else {
                System.out.println("Can't set current tag field to object of type: " + value.getClass().getName());
            }

            currentList.add(Integer.parseInt(currentField), tag);
        } else { // False -> CompoundTag
            if (value instanceof Tag) {
                currentCompound.put(currentField, (Tag) value);
            } else if (value instanceof String) {
                currentCompound.putString(currentField, (String) value);
            } else if (value instanceof Integer) {
                currentCompound.putInt(currentField, (Integer) value);
            } else if (value instanceof Long) {
                currentCompound.putLong(currentField, (Long) value);
            } else if (value instanceof Float) {
                currentCompound.putFloat(currentField, (Float) value);
            } else if (value instanceof Boolean) {
                currentCompound.putBoolean(currentField, (Boolean) value);
            } else if (value instanceof Double) {
                currentCompound.putDouble(currentField, (Double) value);
            } else if (value instanceof UUID) {
                currentCompound.putUuid(currentField, (UUID) value);
            } else if (value instanceof Short) {
                currentCompound.putShort(currentField, (Short) value);
            } else if (value instanceof Byte) {
                currentCompound.putByte(currentField, (Byte) value);
            } else if (value instanceof int[]) {
                currentCompound.putIntArray(currentField, (int[]) value);
            } else if (value instanceof byte[]) {
                currentCompound.putByteArray(currentField, (byte[]) value);
            } else if (value instanceof long[]) {
                currentCompound.putLongArray(currentField, (long[]) value);
            } else {
                System.out.println("Can't set current tag field to object of type: " + value.getClass().getName());
            }
        }
    }

    public static void clearPlayerCurrentConfig(UUID playerUUID) {
        CURRENT_EDITED_CONFIG.remove(playerUUID);
    }
}
