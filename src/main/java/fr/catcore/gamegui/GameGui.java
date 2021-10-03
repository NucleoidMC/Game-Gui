package fr.catcore.gamegui;

import com.google.common.reflect.Reflection;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameGui implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Reflection.initialize(GameGuiCustomItems.class);

//        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
//            GameGuiCommand.register(dispatcher);
//        });
    }
}
