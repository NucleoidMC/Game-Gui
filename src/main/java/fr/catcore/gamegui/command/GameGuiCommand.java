package fr.catcore.gamegui.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import fr.catcore.gamegui.item.CreateGameItem;
import fr.catcore.gamegui.item.JoinGameItem;
import fr.catcore.gamegui.item.OpenGameItem;
import fr.catcore.gamegui.util.Utils;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class GameGuiCommand {

    public static final SimpleCommandExceptionType NOT_ENOUGH_PERMISSION = new SimpleCommandExceptionType(
            new LiteralText("You do not have the permission to use this command!")
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("gamegui")
                .then(CommandManager.literal("join").executes(GameGuiCommand::joinGame))
                .then(CommandManager.literal("open").executes(GameGuiCommand::openGame))
                .then(CommandManager.literal("create").executes(GameGuiCommand::createGame)));
    }

    private static int joinGame(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        JoinGameItem.openJoinScreen(player);
        return Command.SINGLE_SUCCESS;
    }

    private static int openGame(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (!Utils.hasOpenPermission(player)) throw NOT_ENOUGH_PERMISSION.create();
        OpenGameItem.openOpenScreen(player);
        return Command.SINGLE_SUCCESS;
    }

    private static int createGame(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (!Utils.hasOpenPermission(player)) throw NOT_ENOUGH_PERMISSION.create();
        CreateGameItem.openOpenScreen(player);
        return Command.SINGLE_SUCCESS;
    }

}
