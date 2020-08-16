package fr.catcore.gamegui.item;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.util.Utils;
import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import fr.catcore.gamegui.ui.OpenGameTypeUi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.game.GameType;

public final class OpenGameItem extends Item implements FakeItem {
    public OpenGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return new LiteralText("Open Game");
    }

    @Override
    public Text getName() {
        return new LiteralText("Open Game");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!Utils.hasOpenPermission((ServerPlayerEntity) playerEntity)) return TypedActionResult.fail(playerEntity.getStackInHand(hand));
        playerEntity.openHandledScreen(OpenGameTypeUi.create(new LiteralText("Open Game"), openGameTypeBuilder -> {
            for (Identifier gameType : GameType.REGISTRY.keySet()) {
                openGameTypeBuilder.add(OpenGameTypeEntry.ofItem(GameGui.getGameInfos(gameType).getIcon()).withGameType(gameType));
            }
        }));
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    @Override
    public ItemStack asProxy(ItemStack stack) {
        stack.setCustomName(this.getName(stack));
        return stack;
    }

    @Override
    public Item asProxy() {
        return Items.PAPER;
    }
}
