package fr.catcore.gamegui.inventory.codec;

import fr.catcore.gamegui.builder.gamebuilder.CodecGuiBuilder;
import fr.catcore.gamegui.builder.gamebuilder.CodecGuiEntry;
import fr.catcore.gamegui.builder.gamebuilder.EmptyGuiEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CodecGuiInventory<E extends CodecGuiEntry> implements Inventory {
    private final List<CodecGuiEntry> elements;
    private final ServerPlayerEntity player;
    private final Consumer<CodecGuiBuilder> builder;

    public CodecGuiInventory(ServerPlayerEntity player, Consumer<CodecGuiBuilder> builder) {
        this.elements = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            this.elements.add(new EmptyGuiEntry());
        }
        this.player = player;
        this.builder = builder;
        this.buildGrid();
    }

    public abstract List<Integer> getFillableSlots();

    private void buildGrid() {
        CodecGuiBuilder builder = new CodecGuiBuilder();
        this.builder.accept(builder);
        this.buildGrid(builder.getElements());
    }

    private void buildGrid(List<CodecGuiEntry> elements) {
        this.fill(new EmptyGuiEntry());

        for (int index = 0; index < this.getFillableSlots().size(); index++) {
            this.elements.set(this.getFillableSlots().get(index), elements.get(index));
        }
        for (CodecGuiEntry element : this.elements) {
            System.out.println(element.getClass().getName());
        }
    }

    private void fill(CodecGuiEntry elm) {
        for (int i = 0; i < this.size(); i++) {
            try {
                this.elements.set(i, elm);
            } catch (IndexOutOfBoundsException e) {
                this.elements.add(elm);
            }
        }
    }

    private List<E> resolveRow(List<E> elements, int row) {
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

    public boolean isEmpty() {
        return false;
    }

    public int getMaxCountPerStack() {
        return 1;
    }

    public ItemStack getStack(int index) {
        CodecGuiEntry element = this.elements.get(index);
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
        System.out.println(index);
        this.player.inventory.setCursorStack(ItemStack.EMPTY);
        this.player.updateCursorStack();
        CodecGuiEntry element = this.elements.get(index);
        System.out.println(element);
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
