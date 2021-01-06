package fr.catcore.gamegui.mixin.codec;

import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.catcore.gamegui.codec.GameTypeCoderNBTRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RecordCodecBuilder.class, remap = false, priority = 2000)
public class MixinRecordCodecBuilder {

    @Redirect(method = "build", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;unbox(Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;"))
    private static <O, F> RecordCodecBuilder<?, ?> build_get_record(App<RecordCodecBuilder.Mu<O>, O> box) {
        RecordCodecBuilder<?, ?> codecBuilder = RecordCodecBuilder.unbox(box);
        GameTypeCoderNBTRegistry.recordCoderBuilder(codecBuilder);
        System.out.println(codecBuilder);
        return codecBuilder;
    }

    @Inject(method = "build", at = @At("RETURN"), cancellable = true)
    private static <O, F> void build_get_map(App<RecordCodecBuilder.Mu<O>, O> builderBox, CallbackInfoReturnable<MapCodec<O>> cir) {
        MapCodec<O> mapCodec = cir.getReturnValue();
        System.out.println(mapCodec);
        GameTypeCoderNBTRegistry.mapCodec(mapCodec);
    }

    @Mixin(targets = "com/mojang/serialization/codecs/RecordCodecBuilder$2", remap = false)
    public static class MixinRecordCodecBuilder2 {

        @Inject(method = "<init>", at = @At("RETURN"))
        private void mixin_ctr(RecordCodecBuilder<?, ?> builder) {
            System.out.println("Im' i  the builder!");
        }

        @Redirect(method = "decode", at = @At(value = "FIELD", opcode = 180, remap = false))
        private RecordCodecBuilder<?, ?> inject_in_decode(RecordCodecBuilder<?, ?> builder) {
            GameTypeCoderNBTRegistry.recordCoderBuilder(builder);
            GameTypeCoderNBTRegistry.mapCodec((MapCodec<?>) (Object) this);
            System.out.println("AAAAAAAAAAAA");
            return builder;
        }
    }
}
