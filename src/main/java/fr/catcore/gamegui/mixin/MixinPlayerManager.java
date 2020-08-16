package fr.catcore.gamegui.mixin;

import com.mojang.brigadier.tree.CommandNode;
import fr.catcore.gamegui.GameGuiCustomItems;
import net.minecraft.item.ItemStack;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Shadow @Final private MinecraftServer server;

    @Inject(at = @At("RETURN"), method = "onPlayerConnect")
    private void addCustomItem(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        ItemStack openGame = new ItemStack(GameGuiCustomItems.OPEN_GAME);
        if (!player.inventory.contains(openGame) && this.hasOpenPermission(player)) {
            player.inventory.insertStack(openGame);
        }

        ItemStack joinGame = new ItemStack(GameGuiCustomItems.JOIN_GAME);
        if (!player.inventory.contains(joinGame)) {
            player.inventory.insertStack(joinGame);
        }
    }
    
    private boolean hasOpenPermission(ServerPlayerEntity player) {
        CommandNode<ServerCommandSource> openNode = this.getGameOpenNode();

        if (openNode != null) {
            ServerCommandSource source = new ServerCommandSource(
                    player, player.getPos(), player.getRotationClient(),
                    (ServerWorld) player.world,
                    player.server.getPermissionLevel(player.getGameProfile()),
                    player.getName().toString(), player.getDisplayName(),
                    player.server, player
            );

            return openNode.getRequirement().test(source);
        } else {
            return player.hasPermissionLevel(2);
        }
    }

    private CommandNode<ServerCommandSource> getGameOpenNode() {
        Collection<CommandNode<ServerCommandSource>> nodes = this.server.getCommandManager().getDispatcher().getRoot().getChildren();

        for (CommandNode<ServerCommandSource> node : nodes) {
            if (node.getName().equals("game")) {
                Collection<CommandNode<ServerCommandSource>> children = node.getChildren();
                for (CommandNode<ServerCommandSource> child : children) {
                    if (child.getName().equals("open")) {
                        return child;
                    }
                }
            }
        }

        return null;
    }
}
