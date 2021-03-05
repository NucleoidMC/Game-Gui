package fr.catcore.gamegui.codec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CodecNBTSimplifier {

    private static final Logger LOGGER = LogManager.getLogger();
    private CompoundTag codecNBT;
    private CompoundTag simplifiedNBT;

    private CodecNBTSimplifier(CompoundTag codecNBT) {
        this.codecNBT = codecNBT;
        this.simplifiedNBT = this.simplify(this.codecNBT);
    }

    private CompoundTag simplify(CompoundTag tag) {
        CompoundTag compoundTag = new CompoundTag();

        tag.putString("type", "object");
        tag.putBoolean("optional", false);

        for (String key: tag.getKeys()) {
            if (key.equals("unit_decoder") || key.equals("key") || key.equals("type")) continue;
            compoundTag.put(key, this.simplifySub(tag.get(key)));
        }

        return compoundTag;
    }

    private Tag simplifySub(Tag tag) {
        String type;
        if (tag.getType() == 10) {
            CompoundTag compoundTag = (CompoundTag) tag;
            type = compoundTag.getString("type");

            switch (type) {
                case "optional_field":
                    CompoundTag optionalField = new CompoundTag();
                    optionalField.putBoolean("optional", true);
                    String optionalFieldType = compoundTag.getCompound("value_type").getString("type");

                    switch (optionalFieldType) {
                        case "list_codec":
                            optionalField.putString("type", "list");
                            optionalField.put("list_type", this.simplifySub(compoundTag.getCompound("value_type").getCompound("value")));
                            break;
                        case "either_codec":
                            optionalField.putString("type", "either");
                            optionalField.put("left", this.simplifySub(compoundTag.getCompound("value_type").getCompound("left")));
                            optionalField.put("right", this.simplifySub(compoundTag.getCompound("value_type").getCompound("right")));
                            break;
                        case "registry":
                            optionalField.putString("type", "resource_key");
                            optionalField.putString("registry", compoundTag.getCompound("value_type").getCompound("value").getString("value"));
                            break;
                        case "key_dispatch_codec":
                            optionalField.putString("type", "resource_key");
                            optionalField.putString("registry", ((CompoundTag) this.simplifySub(compoundTag.getCompound("value_type"))).getString("registry"));
                            break;
                        case "record_codec":
                            optionalField.putString("type", "object");
                            optionalField.put("object_type", this.simplifySub(compoundTag.getCompound("value_type")));
                            break;
                        default:
                            optionalField.putString("type", optionalFieldType);
                            break;
                    }
                    return optionalField;

                case "field":
                    CompoundTag field = new CompoundTag();
                    field.putBoolean("optional", false);
                    String fieldType = compoundTag.getCompound("value_type").getString("type");

                    switch (fieldType) {
                        case "list_codec":
                            field.putString("type", "list");
                            field.put("list_type", this.simplifySub(compoundTag.getCompound("value_type").getCompound("value")));
                            break;
                        case "either_codec":
                            field.putString("type", "either");
                            field.put("left", this.simplifySub(compoundTag.getCompound("value_type").getCompound("left")));
                            field.put("right", this.simplifySub(compoundTag.getCompound("value_type").getCompound("right")));
                            break;
                        case "registry":
                            field.putString("type", "resource_key");
                            field.putString("registry", compoundTag.getCompound("value_type").getCompound("value").getString("value"));
                            break;
                        case "key_dispatch_codec":
                            field.putString("type", "resource_key");
                            field.putString("registry", ((CompoundTag) this.simplifySub(compoundTag.getCompound("value_type"))).getString("registry"));
                            break;
                        case "record_codec":
                            field.putString("type", "object");
                            field.put("object_type", this.simplifySub(compoundTag.getCompound("value_type")));
                            break;
                        default:
                            field.putString("type", fieldType);
                            break;
                    }

                    return field;
                case "key_dispatch_codec":
                    CompoundTag keyDispatchCodec = new CompoundTag();
                    keyDispatchCodec.putString("type", "resource_key");
                    keyDispatchCodec.putString("registry", compoundTag.getCompound("value").getCompound("value").getString("value"));

                    return keyDispatchCodec;
                case "registry":
                    CompoundTag registry = new CompoundTag();
                    registry.putString("type", "resource_key");
                    registry.putString("registry", compoundTag.getCompound("value").getString("value"));

                    return registry;
                case "record_codec":
                    CompoundTag recordCodec = new CompoundTag();

                    for (String key : compoundTag.getKeys()) {
                        if (key.equals("unit_decoder") || key.equals("key") || key.equals("type")) continue;
                        recordCodec.put(key, this.simplifySub(compoundTag.get(key)));
                    }

                    return recordCodec;
                case "either_codec":
                    CompoundTag eitherTag = new CompoundTag();
                    eitherTag.putString("type", "either");
                    eitherTag.put("left", this.simplifySub(compoundTag.getCompound("left")));
                    eitherTag.put("right", this.simplifySub(compoundTag.getCompound("right")));

                    return eitherTag;
                case "list_codec":
                    CompoundTag listCodecTag = new CompoundTag();
                    listCodecTag.putString("type", "list");
                    listCodecTag.put("list_type", this.simplifySub(compoundTag.getCompound("value")));
                    return listCodecTag;
                default:
                    if (!compoundTag.contains("type")) {
                        compoundTag.putString("type", "object");
                        return this.simplify(compoundTag);
                    }
                    CompoundTag defaultTag = new CompoundTag();
                    defaultTag.putString("type", type);
                    return defaultTag;

            }
        }
        return tag;
    }

    public static CompoundTag simplifyCodecNBT(CompoundTag codecNBT) {
        CodecNBTSimplifier codecNBTSimplifier = new CodecNBTSimplifier(codecNBT);
        if (codecNBTSimplifier.simplifiedNBT.contains("optional")) {
            codecNBTSimplifier.simplifiedNBT.remove("optional");
        }
        return codecNBTSimplifier.simplifiedNBT;
    }
}
