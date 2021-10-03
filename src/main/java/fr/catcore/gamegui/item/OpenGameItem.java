package fr.catcore.gamegui.item;

import eu.pb4.polymer.item.VirtualItem;
import fr.catcore.gamegui.util.Utils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public final class OpenGameItem extends Item implements VirtualItem {
    public OpenGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText("text.game_gui.gui.open");
    }

    @Override
    public Text getName() {
        return new TranslatableText("text.game_gui.gui.open");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!Utils.hasOpenPermission((ServerPlayerEntity) playerEntity)) return TypedActionResult.fail(playerEntity.getStackInHand(hand));
        openOpenScreen(playerEntity);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public static void openOpenScreen(PlayerEntity playerEntity, int page) {

    }

    public static void openOpenScreen(PlayerEntity playerEntity) {
        openOpenScreen(playerEntity, 0);
    }

    @Override
    public Item getVirtualItem() {
        return Items.PAPER;
    }
}
