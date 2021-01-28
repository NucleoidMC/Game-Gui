package fr.catcore.gamegui.codec;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;

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

    public static CompoundTag getCodecNBT(Identifier gameType, String stringCodec) {
        if (!GAME_TYPE_TO_CODEC_NBT.containsKey(gameType.toString())) GAME_TYPE_TO_CODEC_NBT.put(gameType.toString(), CodecToNBTParser.parse(gameType.toString(), stringCodec));

        return GAME_TYPE_TO_CODEC_NBT.get(gameType.toString());
    }

    public static CompoundTag getCodecNBT(GameType<?> gameType) {
        return getCodecNBT(gameType.getIdentifier(), ((MapCodec.MapCodecCodec) gameType.getConfigCodec()).codec().toString());
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
