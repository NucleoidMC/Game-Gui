package fr.catcore.gamegui.ui.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CallBackUi implements NamedScreenHandlerFactory {

    private final Text title;
    private final Consumer<CodecGuiBuilder> builder;
    private final Function<Void, NamedScreenHandlerFactory> callback;

    protected CallBackUi(Text title, Consumer<CodecGuiBuilder> builder, Function<Void, NamedScreenHandlerFactory> callback) {
        this.title = title;
        this.builder = builder;
        this.callback = callback;
    }

    public Function<Void, NamedScreenHandlerFactory> getCallback() {
        return callback;
    }

    public Consumer<CodecGuiBuilder> getBuilder() {
        return builder;
    }

    @Override
    public Text getDisplayName() {
        return this.title;
    }
}
