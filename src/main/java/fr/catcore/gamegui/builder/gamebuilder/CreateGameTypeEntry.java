package fr.catcore.gamegui.builder.gamebuilder;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.codec.GameTypeCoderNBTRegistry;
import fr.catcore.gamegui.inventory.codec.ConfigureGameMainInventory;
import fr.catcore.gamegui.ui.codec.ConfigureGameMainUi;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public class CreateGameTypeEntry extends GuiEntry {
    private GameType<?> gameType;

    public CreateGameTypeEntry(ItemStack icon) {
        super(icon);
    }

    public CreateGameTypeEntry withGameType(GameType<?> gameType) {
        this.gameType = gameType;
        return this;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(this.getIcon());
        builder.setName(this.gameType.getName());
        for (Text text : GameGui.getGameTypeDescription(this.gameType.getIdentifier())) {
            builder.addLore(text);
        }
        return builder.build().copy();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        CompoundTag codecNBT = GameTypeCoderNBTRegistry.getCodecNBT(this.gameType);
        if (codecNBT.getKeys().size() == 0) return;
        System.out.println(codecNBT.toString());
        CompoundTag currentConfig = new CompoundTag();
        currentConfig.putString("type", this.gameType.getIdentifier().toString());
        currentConfig.putString("name", this.gameType.getName().getString());

        GameCreatorHelper.startCreatingConfig(player.getUuid(), currentConfig);

//        player.closeHandledScreen();
//        player.openHandledScreen(ConfigureGameMainUi.create(new LiteralText("Configure Game: Main"), mainGuiEntryCodecGuiBuilder -> {
//            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createType());
//            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createName());
//            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createConfig());
//            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createLaunch());
//        }));

        ConfigureGameMainInventory configureGameMainInventory = new ConfigureGameMainInventory(player, mainGuiEntryCodecGuiBuilder -> {
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createType());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createName());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createConfig());
            mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createLaunch());
        });
        ((GenericContainerScreenHandlerAccessor)player.currentScreenHandler).setInventory(configureGameMainInventory, player);

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
