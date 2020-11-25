package fr.catcore.gamegui.item;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.util.Utils;
import fr.catcore.gamegui.builder.OpenGameTypeEntry;
import fr.catcore.gamegui.ui.OpenGameTypeUi;
import fr.catcore.server.translations.api.LocalizableText;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
        playerEntity.openHandledScreen(OpenGameTypeUi.create(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.gui.open"),
                (LocalizationTarget) playerEntity), openGameTypeBuilder -> {
            for (Identifier gameType : GameType.REGISTRY.keySet()) {
                if (gameType.toString().equals("plasmid:test")) continue;
                openGameTypeBuilder.add(OpenGameTypeEntry.ofItem(GameGui.getGameInfos(gameType).getIcon()).withGameType(gameType));
            }
        }));
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    @Override
    public Item asProxy() {
        return Items.PAPER;
    }
}
