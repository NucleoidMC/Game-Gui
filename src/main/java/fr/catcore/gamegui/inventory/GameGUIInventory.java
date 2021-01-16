package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.GuiBuilder;
import fr.catcore.gamegui.builder.GuiEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GameGUIInventory<B extends GuiBuilder, E extends GuiEntry> implements Inventory {
    private static final int WIDTH = 9;
    private static final int PADDING = 1;
    private static final int PADDED_WIDTH = 7;
    private final List<E> elements;
    private final ServerPlayerEntity player;
    private final Consumer<B> builder;

    public GameGUIInventory(ServerPlayerEntity player, Consumer<B> builder) {
        this.elements = new ArrayList<E>(this.size());
        this.player = player;
        this.builder = builder;
        this.buildGrid();
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public List<E> getElements() {
        try {
            B builder = (B) ((Class) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
            this.builder.accept(builder);
            return builder.getElements();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    protected void buildGrid() {
        this.buildGrid(this.getElements());
    }

    protected void buildGrid(List<E> elements) {
        this.fill(null);
        int rows = MathHelper.ceil((double)elements.size() / 7.0D);

        for(int row = 0; row < rows; ++row) {
            List<E> resolved = this.resolveRow(elements, row);
            int minColumn = (9 - resolved.size()) / 2;

            for(int column = 0; column < resolved.size(); ++column) {
                E element = resolved.get(column);
                this.elements.set(column + minColumn + row * 9, element);
            }
        }

    }

    protected void fill(E elm) {
        for (int i = 0; i < this.size(); i++) {
            try {
                this.elements.set(i, elm);
            } catch (IndexOutOfBoundsException e) {
                this.elements.add(elm);
            }
        }
    }

    protected List<E> resolveRow(List<E> elements, int row) {
        int minId = 2147483647;
        int maxId = -2147483648;
        int rowStart = row * 7;
        int rowEnd = Math.min(rowStart + 7, elements.size());

        for(int idx = rowStart; idx < rowEnd; ++idx) {
            if (elements.get(idx) != null) {
                if (idx < minId) {
                    minId = idx;
                }

                if (idx > maxId) {
                    maxId = idx;
                }
            }
        }

        List<E> resolved = new ArrayList<>(maxId - minId + 1);
        for (int i = minId; i < minId + (maxId - minId + 1); i++) {
            resolved.add(elements.get(i));
        }
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
        E element = this.getElement(index);
        return element == null ? ItemStack.EMPTY : element.createIcon(this.player);
    }

    public E getElement(int index) {
        return this.elements.get(index);
    }

    public ItemStack removeStack(int index, int count) {
        this.handleElementClick(index);
        return ItemStack.EMPTY;
    }

    public ItemStack removeStack(int index) {
        this.handleElementClick(index);
        return ItemStack.EMPTY;
    }

    protected void handleElementClick(int index) {
        this.player.inventory.setCursorStack(ItemStack.EMPTY);
        this.player.updateCursorStack();
        E element = this.getElement(index);
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
