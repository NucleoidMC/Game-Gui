package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.GameGuiCustomItems;
import fr.catcore.gamegui.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void addCustomItem(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ItemStack openGame = new ItemStack(GameGuiCustomItems.OPEN_GAME);
        if (!player.getInventory().contains(openGame) && Utils.hasOpenPermission(player)) {
            player.getInventory().insertStack(openGame);
        }

        ItemStack joinGame = new ItemStack(GameGuiCustomItems.JOIN_GAME);
        if (!player.getInventory().contains(joinGame)) {
            player.getInventory().insertStack(joinGame);
        }
    }
}
