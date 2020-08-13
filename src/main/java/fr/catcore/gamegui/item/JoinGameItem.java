package fr.catcore.gamegui.item;

import fr.catcore.gamegui.builder.JoinGameEntry;
import fr.catcore.gamegui.ui.JoinGameUi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import xyz.nucleoid.plasmid.fake.FakeItem;
import xyz.nucleoid.plasmid.game.ConfiguredGame;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.config.GameConfigs;

public final class JoinGameItem extends Item implements FakeItem<Item> {

    public JoinGameItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getFaking(ItemStack stack) {
        ItemStack fake = FakeItem.super.getFaking(stack);
        fake.setCustomName(stack.getName());
        return fake;
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
        playerEntity.openHandledScreen(JoinGameUi.create(new LiteralText("Join Game"), joinGameBuilder -> {
            for (ServerWorld serverWorld : playerEntity.getServer().getWorlds()) {
                GameWorld gameWorld = GameWorld.forWorld(serverWorld);
                if (gameWorld == null) continue;
                ConfiguredGame<?> configuredGame = gameWorld.getGame();
                Identifier gameID = null;
                for (Identifier id : GameConfigs.getKeys()) {
                    if (GameConfigs.get(id) == configuredGame) {
                        gameID = id;
                        break;
                    }
                }
                joinGameBuilder.add(JoinGameEntry.ofItem(Items.CONDUIT).withDimensionKey(serverWorld.getRegistryKey()).withGameConfig(gameID));
            }
        }));
        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }

    @Override
    public Item getFaking() {
        return Items.COMPASS;
    }
}
