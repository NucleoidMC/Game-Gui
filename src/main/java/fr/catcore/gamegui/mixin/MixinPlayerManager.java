package fr.catcore.gamegui.mixin;

import com.mojang.brigadier.tree.CommandNode;
import fr.catcore.gamegui.GameGuiCustomItems;
import net.fabricmc.loader.api.FabricLoader;
import net.gegy1000.roles.override.command.CommandPermEvaluator;
import net.gegy1000.roles.override.command.MatchableCommand;
import net.gegy1000.roles.override.command.PermissionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        if (FabricLoader.getInstance().isModLoaded("player-roles")) {
            ServerCommandSource serverCommandSource = new ServerCommandSource(player, player.getPos(), player.getRotationClient(), player.world instanceof ServerWorld ? (ServerWorld) player.world : null,
                    player.server.getPermissionLevel(player.getGameProfile()), player.getName().toString(), player.getDisplayName(), player.server, player);
            Collection<CommandNode<ServerCommandSource>> nodes = server.getCommandManager().getDispatcher().getRoot().getChildren();
            CommandNode<ServerCommandSource> open = null;
            for (CommandNode<ServerCommandSource> node : nodes) {
                if (node.getName().equals("game")) {
                    Collection<CommandNode<ServerCommandSource>> children = node.getChildren();
                    for (CommandNode<ServerCommandSource> child : children) {
                        if (child.getName().equals("open")) {
                            open = node;
                            break;
                        }
                    }
                }
            }
            if (open != null) {
                MatchableCommand matchableCommand = MatchableCommand.compile(new CommandNode[]{open});
                if (!(player.inventory.contains(openGame) || CommandPermEvaluator.canUseCommand(serverCommandSource, matchableCommand) != PermissionResult.ALLOW)) {
                    player.inventory.insertStack(openGame);
                }
            }
        } else {
            if (!(player.inventory.contains(openGame) || !player.hasPermissionLevel(2))) {
                player.inventory.insertStack(openGame);
            }
        }
        ItemStack joinGame = new ItemStack(GameGuiCustomItems.JOIN_GAME);
        if (!player.inventory.contains(joinGame)) {
            player.inventory.insertStack(joinGame);
        }
    }
}
