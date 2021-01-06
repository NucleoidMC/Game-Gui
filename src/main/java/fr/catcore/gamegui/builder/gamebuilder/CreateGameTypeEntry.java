package fr.catcore.gamegui.builder.gamebuilder;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.codec.CodecToStringParser;
import fr.catcore.gamegui.codec.RecoderCoderBuilderRegistry;
import fr.catcore.gamegui.mixin.codec.RecordCodecBuilderAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class CreateGameTypeEntry extends GuiEntry {
    private Identifier gameType;

    public CreateGameTypeEntry(ItemStack icon) {
        super(icon);
    }

    public CreateGameTypeEntry withGameType(Identifier gameType) {
        this.gameType = gameType;
        return this;
    }

    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(GameGui.getGameTypeName(this.gameType));
        for (Text text : GameGui.getGameTypeDescription(this.gameType)) {
            builder.addLore(text);
        }
        return builder.build().copy();
    }

    public void onClick(ServerPlayerEntity player) {
        CodecToStringParser.parse(this.gameType.toString(), ((MapCodec.MapCodecCodec)GameType.get(this.gameType).getConfigCodec()).codec().toString());
//        System.out.println(((MapCodec.MapCodecCodec)GameType.get(this.gameType).getConfigCodec()).codec().toString());
//        System.out.println(((MapCodec.MapCodecCodec)GameType.get(this.gameType).getConfigCodec()).codec().getClass().toString());
//        RecordCodecBuilder recordCodecBuilder = (RecordCodecBuilder) (Object) ((MapCodec.MapCodecCodec)GameType.get(this.gameType).getConfigCodec()).codec();
//        RecordCodecBuilderAccessor accessor = (RecordCodecBuilderAccessor) (Object) recordCodecBuilder;
//        System.out.println(accessor.getDecoder().toString());
//        System.out.println(accessor.getEncoder().toString());
//        System.out.println(accessor.getGetter().toString());
//        System.out.println(RecoderCoderBuilderRegistry.getMapToBuilder().toString());
//        System.out.println(RecoderCoderBuilderRegistry.getRecordCodecBuilder(((MapCodec.MapCodecCodec)GameType.get(this.gameType).getConfigCodec()).codec()).toString());
//        player.openHandledScreen(OpenConfiguredGameUi.create(new TranslatableText("text.game_gui.gui.open"),
//                openConfiguredGameBuilder -> {
//            Identifier[] configs = GameGui.getConfigsFromType(this.gameType);
//            for (Identifier configuredGame : configs) {
//                openConfiguredGameBuilder.add(GuiEntry
//                        .openConfiguredGameEntryOf(GameGui.getGameInfos(this.gameType).get())
//                        .withGameConfig(configuredGame));
//            }
//        }));
    }
}
