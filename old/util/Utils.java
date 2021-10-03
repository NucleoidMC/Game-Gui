package fr.catcore.gamegui.util;

import com.mojang.brigadier.tree.CommandNode;
import fr.catcore.gamegui.accessor.ForgingScreenHandlerAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Collection;

public class Utils {

    public static boolean hasOpenPermission(ServerPlayerEntity player) {
        CommandNode<ServerCommandSource> openNode = getGameOpenNode(player);

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

    private static CommandNode<ServerCommandSource> getGameOpenNode(ServerPlayerEntity player) {
        Collection<CommandNode<ServerCommandSource>> nodes = player.server.getCommandManager().getDispatcher().getRoot().getChildren();

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
