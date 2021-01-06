package fr.catcore.gamegui.codec;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTypeCoderNBTRegistry {

    private static final Map<MapCodec<?>, RecordCodecBuilder<?, ?>> MAP_TO_BUILDER = new HashMap<>();
    private static final List<MapCodec<?>> MAP_CODECS = new ArrayList<>();
    private static final List<RecordCodecBuilder<?, ?>> RECORD_CODEC_BUILDERS = new ArrayList<>();

    private static final Map<String, CompoundTag> GAME_TYPE_TO_CODEC_NBT = new HashMap<>();

    public static void recordCoderBuilder(RecordCodecBuilder<?, ?> builder) {
        RECORD_CODEC_BUILDERS.add(builder);
    }

    public static CompoundTag getCodecNBT(String gameType, String stringCodec) {
        if (!GAME_TYPE_TO_CODEC_NBT.containsKey(gameType)) GAME_TYPE_TO_CODEC_NBT.put(gameType, CodecToNBTParser.parse(gameType, stringCodec));

        return GAME_TYPE_TO_CODEC_NBT.get(gameType);
    }

    public static void mapCodec(MapCodec<?> mapCodec) {
        MAP_CODECS.add(mapCodec);

        MAP_TO_BUILDER.put(MAP_CODECS.get(MAP_CODECS.size() - 1), RECORD_CODEC_BUILDERS.get(RECORD_CODEC_BUILDERS.size() - 1));
    }

    public static RecordCodecBuilder<?, ?> getRecordCodecBuilder(MapCodec<?> mapCodec) {
        return MAP_TO_BUILDER.get(mapCodec);
    }

    public static Map<MapCodec<?>, RecordCodecBuilder<?, ?>> getMapToBuilder() {
        return MAP_TO_BUILDER;
    }
}
