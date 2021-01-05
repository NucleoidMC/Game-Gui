package fr.catcore.gamegui.mixin.codec;

import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(RecordCodecBuilder.class)
public interface RecordCodecBuilderAccessor<O, F> {

    @Accessor("decoder")
    MapDecoder<F> getDecoder();

    @Accessor("encoder")
    Function<O, MapEncoder<F>> getEncoder();

    @Accessor("getter")
    Function<O, F> getGetter();
}
