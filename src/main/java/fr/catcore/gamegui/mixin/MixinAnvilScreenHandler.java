package fr.catcore.gamegui.mixin;

import fr.catcore.gamegui.accessor.AnvilScreenHandlerAccessor;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnvilScreenHandler.class)
public class MixinAnvilScreenHandler implements AnvilScreenHandlerAccessor {
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Shadow private String newItemName;

    @Override
    public int getLevelCost_server() {
        return this.levelCost.get();
    }

    @Override
    public void setLevelCost_server(int cost) {
        this.levelCost.set(cost);
    }

    @Override
    public int getRepairItemUsage() {
        return this.repairItemUsage;
    }

    @Override
    public void setRepairItemUsage(int repairItemUsage) {
        this.repairItemUsage = repairItemUsage;
    }

    @Override
    public String getNewItemName() {
        return this.newItemName;
    }
}
