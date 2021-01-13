package fr.catcore.gamegui.builder.gamebuilder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.codec.GameTypeCoderNBTRegistry;
import fr.catcore.gamegui.ui.codec.ConfigureGameMainUi;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
        CompoundTag codecNBT = GameTypeCoderNBTRegistry.getCodecNBT(this.gameType);
        if (codecNBT.getKeys().size() == 0) return;

        CompoundTag currentConfig = new CompoundTag();
        currentConfig.putString("type", this.gameType.toString());
        currentConfig.putString("name", this.gameType.toString());

        GameCreatorHelper.startCreatingConfig(player.getUuid(), currentConfig);

        player.closeHandledScreen();
        player.openHandledScreen(ConfigureGameMainUi.create(new LiteralText("Configure Game: Main"), mainGuiEntryCodecGuiBuilder -> {
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createType());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createName());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createConfig());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createLaunch());
        }));

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
