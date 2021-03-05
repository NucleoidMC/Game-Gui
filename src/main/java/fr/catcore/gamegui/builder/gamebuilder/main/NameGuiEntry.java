package fr.catcore.gamegui.builder.gamebuilder.main;

import fr.catcore.gamegui.builder.gamebuilder.property.string.StringGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.ui.codec.ConfigureGameMainUi;
import fr.catcore.gamegui.ui.codec.StringPropertyEditingUi;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.HashMap;
import java.util.function.Function;

public class NameGuiEntry extends PropertyMainGuiEntry {
    protected NameGuiEntry() {
        super(new ItemStack(Items.ANVIL), new LiteralText("Config Name"), "name");
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        ItemStackBuilder builder = ItemStackBuilder.of(super.createIcon(player));
        return builder.build();
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        GameCreatorHelper.setCurrentlyEditedPath(player.getUuid(), new HashMap<>(), "name");

        Function<Void, NamedScreenHandlerFactory> callback = (_void) -> {
            return ConfigureGameMainUi.create(new LiteralText("Configure Game: Main"), codecGuiBuilder -> {
                codecGuiBuilder.add(MainGuiEntry.createType());
                codecGuiBuilder.add(MainGuiEntry.createName());
                codecGuiBuilder.add(MainGuiEntry.createConfig());
                codecGuiBuilder.add(MainGuiEntry.createLaunch());
            });
        };

        player.openHandledScreen(new StringPropertyEditingUi(new LiteralText("Test"), mainGuiEntryCodecGuiBuilder -> {
            mainGuiEntryCodecGuiBuilder.add(StringGuiEntry.createEditedString());
            mainGuiEntryCodecGuiBuilder.add(StringGuiEntry.createCancelString(callback));
        }, callback));
    }
}
