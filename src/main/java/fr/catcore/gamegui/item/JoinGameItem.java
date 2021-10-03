package fr.catcore.gamegui.item;

import eu.pb4.polymer.item.VirtualItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public final class JoinGameItem extends Item implements VirtualItem {

    public JoinGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return new TranslatableText("text.game_gui.gui.join");
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText("text.game_gui.gui.join");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        openJoinScreen(playerEntity);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public static void openJoinScreen(PlayerEntity playerEntity) {
        playerEntity.getServer().getCommandManager().execute(playerEntity.getCommandSource(), "/game join");
    }

    @Override
    public Item getVirtualItem() {
        return Items.COMPASS;
    }
}
