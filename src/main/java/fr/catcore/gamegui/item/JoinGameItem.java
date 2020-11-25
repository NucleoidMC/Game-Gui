package fr.catcore.gamegui.item;

import fr.catcore.gamegui.GameGui;
import fr.catcore.gamegui.builder.JoinGameEntry;
import fr.catcore.gamegui.ui.JoinGameUi;
import fr.catcore.server.translations.api.LocalizableText;
import fr.catcore.server.translations.api.LocalizationTarget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.config.GameConfigs;

public final class JoinGameItem extends Item implements FakeItem {

    public JoinGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return new LiteralText("Join Game");
    }

    @Override
    public Text getName(ItemStack stack) {
        return new LiteralText("Join Game");
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        openJoinScreen(playerEntity);
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    public static void openJoinScreen(PlayerEntity playerEntity) {
        playerEntity.openHandledScreen(JoinGameUi.create(LocalizableText.asLocalizedFor(new TranslatableText("text.game_gui.gui.join"), (LocalizationTarget) playerEntity), joinGameBuilder -> {
            for (ServerWorld serverWorld : playerEntity.getServer().getWorlds()) {
                ManagedGameSpace gameWorld = ManagedGameSpace.forWorld(serverWorld);
                if (gameWorld == null) continue;
                ConfiguredGame<?> configuredGame = gameWorld.getGameConfig();
                Identifier gameID = new Identifier("game-gui", "null");
                for (Identifier id : GameConfigs.getKeys()) {
                    if (GameConfigs.get(id) == configuredGame) {
                        gameID = id;
                        break;
                    }
                }
                joinGameBuilder.add(JoinGameEntry.ofItem(GameGui.getGameInfos(configuredGame.getType().getIdentifier()).getIcon()).withDimensionKey(serverWorld.getRegistryKey()).withGameConfig(gameID));
            }
        }));
    }

    @Override
    public Item asProxy() {
        return Items.COMPASS;
    }
}
