package fr.catcore.gamegui.builder.gamebuilder.main;

import fr.catcore.gamegui.codec.GameCreatorHelper;
import fr.catcore.gamegui.ui.codec.StringPropertyEditingUi;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

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
        System.out.println("Name");
        GameCreatorHelper.setEditingField(player.getUuid(), "name");
        player.openHandledScreen(StringPropertyEditingUi.create(new LiteralText("Test"), mainGuiEntryCodecGuiBuilder -> {}));
    }
}
