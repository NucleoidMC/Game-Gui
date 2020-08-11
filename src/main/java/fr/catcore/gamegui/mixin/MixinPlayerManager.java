package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.GameGuiCustomItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void addCustomItem(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ItemStack openGame = GameGuiCustomItems.OPEN_GAME.applyTo(new ItemStack(Items.FILLED_MAP));
        if (!(player.inventory.contains(openGame) || !player.hasPermissionLevel(2))) {
            player.inventory.insertStack(openGame);
        }
        ItemStack joinGame = GameGuiCustomItems.JOIN_GAME.applyTo(new ItemStack(Items.COMPASS));
        if (!player.inventory.contains(joinGame)) {
            player.inventory.insertStack(joinGame);
        }
    }
}
