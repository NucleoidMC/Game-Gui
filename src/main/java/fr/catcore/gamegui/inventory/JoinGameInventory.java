package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.JoinGameBuilder;
import fr.catcore.gamegui.builder.JoinGameEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.function.Consumer;

public class JoinGameInventory implements Inventory {
    private static final int WIDTH = 9;
    private static final int PADDING = 1;
    private static final int PADDED_WIDTH = 7;
    private final JoinGameEntry[] elements = new JoinGameEntry[this.size()];
    private final ServerPlayerEntity player;
    private final Consumer<JoinGameBuilder> builder;

    public JoinGameInventory(ServerPlayerEntity player, Consumer<JoinGameBuilder> builder) {
        this.player = player;
        this.builder = builder;
        this.buildGrid();
    }

    private void buildGrid() {
        JoinGameBuilder builder = new JoinGameBuilder();
        this.builder.accept(builder);
        this.buildGrid(builder.getElements());
    }

    private void buildGrid(JoinGameEntry[] elements) {
        Arrays.fill(this.elements, (Object)null);
        int rows = MathHelper.ceil((double)elements.length / 7.0D);

        for(int row = 0; row < rows; ++row) {
            JoinGameEntry[] resolved = this.resolveRow(elements, row);
            int minColumn = (9 - resolved.length) / 2;

            for(int column = 0; column < resolved.length; ++column) {
                JoinGameEntry element = resolved[column];
                this.elements[column + minColumn + row * 9] = element;
            }
        }

    }

    private JoinGameEntry[] resolveRow(JoinGameEntry[] elements, int row) {
        int minId = 2147483647;
        int maxId = -2147483648;
        int rowStart = row * 7;
        int rowEnd = Math.min(rowStart + 7, elements.length);

        for(int idx = rowStart; idx < rowEnd; ++idx) {
            if (elements[idx] != null) {
                if (idx < minId) {
                    minId = idx;
                }

                if (idx > maxId) {
                    maxId = idx;
                }
            }
        }

        JoinGameEntry[] resolved = new JoinGameEntry[maxId - minId + 1];
        System.arraycopy(elements, minId, resolved, 0, resolved.length);
        return resolved;
    }

    public int size() {
        return 62;
    }

    public boolean isEmpty() {
        return false;
    }

    public int getMaxCountPerStack() {
        return 1;
    }

    public ItemStack getStack(int index) {
        JoinGameEntry element = this.elements[index];
        return element == null ? ItemStack.EMPTY : element.createIcon(this.player);
    }

    public ItemStack removeStack(int index, int count) {
        this.handleElementClick(index);
        return ItemStack.EMPTY;
    }

    public ItemStack removeStack(int index) {
        this.handleElementClick(index);
        return ItemStack.EMPTY;
    }

    private void handleElementClick(int index) {
        this.player.inventory.setCursorStack(ItemStack.EMPTY);
        this.player.updateCursorStack();
        JoinGameEntry element = this.elements[index];
        if (element != null) {
            element.onClick(this.player);
        }

        this.buildGrid();
        this.player.onHandlerRegistered(this.player.currentScreenHandler, this.player.currentScreenHandler.getStacks());
    }

    public void setStack(int slot, ItemStack stack) {
    }

    public void markDirty() {
    }

    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void clear() {
    }
}
