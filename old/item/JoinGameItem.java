package fr.catcore.gamegui.item;

import fr.catcore.gamegui.GameConfigMetadata;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.ui.JoinGameUi;
import fr.catcore.gamegui.ui.JoinOpenedGameUi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;

public final class JoinGameItem extends Item implements FakeItem {

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
        playerEntity.openHandledScreen(JoinGameUi.create(new TranslatableText("text.game_gui.gui.join")));
    }

    public static void openJoinOpenedScreen(PlayerEntity playerEntity) {
        playerEntity.openHandledScreen(JoinOpenedGameUi.create(new TranslatableText("text.game_gui.gui.join"), joinGameBuilder -> {
            for (ManagedGameSpace gameSpace : ManagedGameSpace.getOpen()) {
                GameConfigMetadata config = GameConfigMetadata.parse(gameSpace.getGameConfig());
                joinGameBuilder.add(
                        GuiEntry.joinGameEntryOf(config)
                                .withGameSpace(gameSpace)
                );
            }
        }));
    }

    @Override
    public Item asProxy() {
        return Items.COMPASS;
    }
}
