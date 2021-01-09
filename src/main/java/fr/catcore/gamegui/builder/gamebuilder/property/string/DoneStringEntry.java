package fr.catcore.gamegui.builder.gamebuilder.property.string;

import fr.catcore.gamegui.accessor.GenericContainerScreenHandlerAccessor;
import fr.catcore.gamegui.builder.gamebuilder.main.MainGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.inventory.codec.StringPropertyInventory;
import fr.catcore.gamegui.ui.codec.ConfigureGameMainUi;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class DoneStringEntry extends StringGuiEntry {
    protected DoneStringEntry() {
        super(new ItemStack(Items.GREEN_TERRACOTTA), new LiteralText("Done"));
    }

    @Override
    public void onClick(ServerPlayerEntity player) {
        if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
            if (inventory instanceof StringPropertyInventory) {
                String newValue = ((StringPropertyInventory)inventory).getCurrentlyEdited();
                GameCreatorHelper.setEditingFieldValue(player.getUuid(), StringTag.of(newValue));
                if (GameCreatorHelper.getEditingField(player.getUuid()).equals("name")) {
                    player.openHandledScreen(ConfigureGameMainUi.create(new LiteralText("Configure Game: Main"), mainGuiEntryCodecGuiBuilder -> {
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createType());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createName());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createConfig());
                        mainGuiEntryCodecGuiBuilder.add(MainGuiEntry.createLaunch());
                    }));
                }
            }
        }
    }
}
