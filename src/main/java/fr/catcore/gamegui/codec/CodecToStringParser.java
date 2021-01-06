package fr.catcore.gamegui.codec;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.nucleoid.plasmid.error.ErrorReporter;

import java.util.ArrayList;
import java.util.List;

public class CodecToStringParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private String mapCodecToString;
    private CompoundTag compoundTag;
    private String gameType;
    private ErrorReporter errorReporter;

    private CodecToStringParser(String gameType, String mapCodecToString) {
        this.gameType = gameType;
        this.errorReporter = ErrorReporter.open("GameGui game config generator with gametype: " + this.gameType);
        this.mapCodecToString = mapCodecToString
                .replace("[xmapped]", "")
                .replace("[mapped]", "")
                .replace("[comapFlatMapped]", "");
        this.compoundTag = this.handle(this.mapCodecToString);
        LOGGER.info(this.compoundTag.toString());
        this.errorReporter.close();
    }

    public static void parse(String gameType, String mapCodecToString) {
        if (!mapCodecToString.startsWith("RecordCodec")) {
            cantHandle(mapCodecToString);
            return;
        }
//

        new CodecToStringParser(gameType, mapCodecToString);

//        LOGGER.info(mapCodecToString);
    }

    private CompoundTag handle(String codecString) {
        if (codecString.startsWith("RecordCodec")) {
            return this.handleRecordCodec(codecString);
        } else if (codecString.startsWith("UnitDecoder")) {
            return this.handleUnitDecoder(codecString);
        } else if (codecString.startsWith("Field")) {
            return this.handleField(codecString);
        } else if (codecString.startsWith("Int")) {
            return this.handleInt(codecString);
        } else if (codecString.startsWith("String")) {
            return this.handleString(codecString);
        } else if (codecString.startsWith("OptionalFieldCodec")) {
            return this.handleOptionalField(codecString);
        } else if (codecString.startsWith("Long")) {
            return this.handleLong(codecString);
        } else if (codecString.startsWith("Bool")) {
            return this.handleBool(codecString);
        } else if (codecString.startsWith("KeyDispatchCodec")) {
            return this.handleKeyDispatchCodec(codecString);
        } else if (codecString.startsWith("Registry")) {
            return this.handleRegistry(codecString);
        } else if (codecString.startsWith("EitherCodec")) {
            return this.handleEitherCodec(codecString);
        } else if (codecString.startsWith("ListCodec")) {
            return this.handleListCodec(codecString);
        } else if (codecString.startsWith("Double")) {
            return this.handleDouble(codecString);
        } else if (codecString.startsWith("passthrough")) {
            return this.handleCompoundTag(codecString);
        }
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            LOGGER.warn("Don't know how to handle " + codecString.split("\\[")[0] +"! (" + codecString + ")");
            LOGGER.warn(this.mapCodecToString);
        }
        errorReporter.report(new Throwable("Don't know how to handle " + codecString.split("\\[")[0] +"! (" + codecString + ")"), "Codec Parser");


        return new CompoundTag();
    }

    private CompoundTag handleRecordCodec(String recordCodec) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "record_codec");
        tag.putString("key", "record_codec");
        recordCodec = recordCodec.substring("RecordCodec".length());
        for (CompoundTag part: this.handleArray(recordCodec)) {
            tag.put(part.getString("key"), part);
        }

        return tag;
    }

    private CompoundTag handleUnitDecoder(String unitDecoder) {
        CompoundTag compoundTag = new CompoundTag();

        unitDecoder = unitDecoder.substring("UnitDecoder".length());
        compoundTag.putString("type", "unit_decoder");
        compoundTag.putString("value", unitDecoder);
        compoundTag.putString("key", "unit_decoder");

        return compoundTag;
    }

    private CompoundTag handleField(String field) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "field");

        field = field.trim().substring("Field".length() + 1, field.length() - 1);
        String key = "";
        for (int i = 0; i < field.length(); i++) {
            if (field.charAt(i) == ':') break;
            key += field.charAt(i);
        }
        tag.putString("key", key);
        field = field.substring(key.length() + 1).trim();

        tag.put("value_type", this.handle(field));

        return tag;
    }

    private CompoundTag handleOptionalField(String field) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "optional_field");

        field = field.trim().substring("OptionalFieldCodec".length() + 1, field.length() - 1);
        String key = "";
        for (int i = 0; i < field.length(); i++) {
            if (field.charAt(i) == ':') break;
            key += field.charAt(i);
        }
        tag.putString("key", key);
        field = field.substring(key.length() + 1).trim();

        tag.put("value_type", this.handle(field));

        return tag;
    }

    private CompoundTag handleListCodec(String listCodec) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "list_codec");
        tag.putString("key", "list_codec");

        listCodec = listCodec.substring("ListCodec[".length(), listCodec.length() - 1);
        tag.put("value", this.handle(listCodec));

        return tag;
    }

    private CompoundTag handleKeyDispatchCodec(String keyDispatchCodec) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "key_dispatch_codec");
        tag.putString("key", "key_dispatch_codec");

        keyDispatchCodec = keyDispatchCodec.substring("KeyDispatchCodec".length() + 1, keyDispatchCodec.length() - 1).trim();
        tag.put("value", this.handle(keyDispatchCodec));

        return tag;
    }

    private CompoundTag handleRegistry(String registry) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "registry");
        tag.putString("key", "registry");

        registry = registry.substring("Registry[".length());

        CompoundTag subTag = new CompoundTag();
        if (registry.startsWith("ResourceKey")) {
            subTag.putString("type", "resource_key");
            subTag.putString("key", "resource_key");

            registry = registry.substring("ResourceKey[".length());
            String value = "";

            for (int i = 0; i < registry.length(); i++) {
                if (registry.charAt(i) == ']') break;
                value += registry.charAt(i);
            }

            subTag.putString("value", value);
        }

        tag.put("value", subTag);

        return tag;
    }

    private CompoundTag handleEitherCodec(String eitherCodec) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "either_codec");
        tag.putString("key", "either_codec");

        eitherCodec = eitherCodec.substring("EitherCodec[".length(), eitherCodec.length() - 1);

        String[] leftAndRight = eitherCodec.split(",");
        tag.put("left", this.handle(leftAndRight[0].trim()));
        tag.put("right", this.handle(leftAndRight[1].trim()));

        return tag;
    }

    private CompoundTag handleInt(String integer) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "integer");

        return tag;
    }

    private CompoundTag handleLong(String Long) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "long");

        return tag;
    }

    private CompoundTag handleDouble(String Double) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "double");

        return tag;
    }

    private CompoundTag handleCompoundTag(String compound) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "compound_tag");

        return tag;
    }

    private CompoundTag handleBool(String bool) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "boolean");

        return tag;
    }

    private CompoundTag handleString(String string) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "string");

        return tag;
    }

    private List<CompoundTag> handleArray(String arrayString) {
        int toGo = 0;
        boolean first = true;
        int i = 0;
        while (i < arrayString.length()) {
            if (toGo < 1 && !first) {
                break;
            }
            if (arrayString.charAt(i) == '[') toGo++;
            else if (arrayString.charAt(i) == ']') toGo--;

            first = false;
            i++;
        }
        String array = arrayString.substring(1, i - 1);
        List<CompoundTag> compoundTags = new ArrayList<>();
//        LOGGER.info("Parsed Array:");
        for (String part: this.seperateArray(array)) {
//            LOGGER.info(" - " + part);
            compoundTags.add(this.handle(part));
        }

        return compoundTags;
    }

    private String[] seperateArray(String array) {
        List<String> parts = new ArrayList<>();

        String current = "";
        int currentPos = 0;
        int depth = 0;
        while (currentPos < array.length()) {
            if (array.charAt(currentPos) == '*' && depth == 0) {
                parts.add(current.trim());
                current = "";
            } else {
                current += array.charAt(currentPos);
                if (array.charAt(currentPos) == '[') depth++;
                else if (array.charAt(currentPos) == ']') depth--;
            }

            currentPos++;
        }

        if (!current.isEmpty()) parts.add(current.trim());

        return parts.toArray(new String[0]);
    }

    private static void cantHandle(String str) {
        LOGGER.error("Can't handle Codec: " + str);
    }
}
