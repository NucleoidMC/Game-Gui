package fr.catcore.gamegui.builder.gamebuilder.main;

import fr.catcore.gamegui.builder.gamebuilder.PropertyGuiEntry;
import fr.catcore.gamegui.codec.GameCreatorHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

public abstract class PropertyMainGuiEntry extends MainGuiEntry implements PropertyGuiEntry {

    private final String propertyName;

    protected PropertyMainGuiEntry(ItemStack icon, Text title, String propertyName) {
        super(icon, title);
        this.propertyName = propertyName;
    }

    @Override
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override
    public ItemStack createIcon(ServerPlayerEntity player) {
        CompoundTag codecTag = GameCreatorHelper.getPlayerCurrentConfig(player.getUuid());
        ItemStackBuilder builder = ItemStackBuilder.of(super.createIcon(player));
        builder.addLore(new LiteralText(codecTag.getString(this.getPropertyName())).formatted(Formatting.DARK_PURPLE));
        return builder.build();
    }
}
