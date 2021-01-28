package fr.catcore.gamegui.item;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.ui.CreateGameTypeUi;
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
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.game.GameType;

public final class CreateGameItem extends Item implements FakeItem {
    public CreateGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return new TranslatableText("text.game_gui.gui.create");
    }

    @Override
    public Text getName() {
        return new TranslatableText("text.game_gui.gui.create");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (!Utils.hasOpenPermission((ServerPlayerEntity) playerEntity)) return TypedActionResult.fail(playerEntity.getStackInHand(hand));
        openOpenScreen(playerEntity);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public static void openOpenScreen(PlayerEntity playerEntity) {
        playerEntity.openHandledScreen(CreateGameTypeUi.create(new TranslatableText("text.game_gui.gui.create"), openGameTypeBuilder -> {
            for (GameType<?> gameType : GameType.REGISTRY.values()) {
                if (!GameGui.hasConfigsForType(gameType)) {
                    continue;
                }
                ItemStack icon = GameGui.getGameInfos(gameType.getIdentifier()).get();
                openGameTypeBuilder.add(GuiEntry.createGameTypeEntryOf(icon).withGameType(gameType));
            }
        }));
    }

    @Override
    public Item asProxy() {
        return Items.PAPER;
    }
}
