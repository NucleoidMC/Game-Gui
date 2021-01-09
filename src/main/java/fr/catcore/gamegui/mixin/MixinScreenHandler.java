package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.accessor.ScreenHandlerAccessor;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ScreenHandler.class)
public abstract class MixinScreenHandler implements ScreenHandlerAccessor {

    @Shadow protected abstract Slot addSlot(Slot slot);

    @Override
    public Slot addSlot_public(Slot slot) {
        return this.addSlot(slot);
    }
}
