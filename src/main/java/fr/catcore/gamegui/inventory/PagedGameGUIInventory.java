package fr.catcore.gamegui.inventory;

import fr.catcore.gamegui.builder.GuiBuilder;
import fr.catcore.gamegui.builder.GuiEntry;
import fr.catcore.gamegui.builder.PageGuiEntry;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import xyz.nucleoid.plasmid.util.ItemStackBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PagedGameGUIInventory<B extends GuiBuilder> extends GameGUIInventory<B, GuiEntry> {

    private int pageIndex = 0;
    private List<GuiEntry> pageElements;

    public PagedGameGUIInventory(ServerPlayerEntity player, Consumer<B> builder, int page) {
        super(player, builder);
        this.pageElements = new ArrayList<>(this.size());
        this.pageIndex = page;
        this.buildGrid();
    }

    public void buildPageGrid() {
        this.fill(null);
        List<GuiEntry> elements = this.getPageElements(this.pageIndex);

        int rows = MathHelper.ceil((double)elements.size() / 7.0D);

        for(int row = 0; row < rows; ++row) {
            List<GuiEntry> resolved = this.resolveRow(elements, row);
            int minColumn = (9 - resolved.size()) / 2;

            for(int column = 0; column < resolved.size(); ++column) {
                GuiEntry element = resolved.get(column);
                this.pageElements.set(column + minColumn + row * 9, element);
            }
        }

        if (this.pageIndex > 0) this.pageElements.set(46, new PageGuiEntry(ItemStackBuilder.of(Items.BROWN_STAINED_GLASS).setName(new LiteralText("<--")).build()) {
            @Override
            public void onClick(ServerPlayerEntity player) {
                PagedGameGUIInventory.this.setPageIndex(PagedGameGUIInventory.this.pageIndex - 1);
            }
        });
        if (this.pageIndex < this.getPageNumber()) this.pageElements.set(52, new PageGuiEntry(ItemStackBuilder.of(Items.GREEN_STAINED_GLASS).setName(new LiteralText("-->")).build()) {
            @Override
            public void onClick(ServerPlayerEntity player) {
                PagedGameGUIInventory.this.setPageIndex(PagedGameGUIInventory.this.pageIndex + 1);
            }
        });
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        this.buildGrid();
        this.getPlayer().onHandlerRegistered(this.getPlayer().currentScreenHandler, this.getPlayer().currentScreenHandler.getStacks());
    }

    @Override
    protected void buildGrid() {
        this.buildPageGrid();
    }

    @Override
    public GuiEntry getElement(int index) {
        return this.pageElements.get(index);
    }

    @Override
    public void fill(GuiEntry elm) {
        if (this.pageElements == null) this.pageElements = new ArrayList<>(this.size());
        for (int i = 0; i < this.size(); i++) {
            try {
                this.pageElements.set(i, elm);
            } catch (IndexOutOfBoundsException e) {
                this.pageElements.add(elm);
            }
        }
    }

    public List<GuiEntry> getPageElements(int pageIndex) {
        List<GuiEntry> eList = new ArrayList<>();
        List<GuiEntry> compelteEList = this.getElements();
        int page = -1;
        for (int i = 0; i < compelteEList.size(); i++) {
            if (i % 28 == 0) page++;

            if (page == pageIndex) {
                eList.add(compelteEList.get(i));
            } else if (page > pageIndex) {
                break;
            }
        }

        return eList;
    }

    public int getPageNumber() {
        List<GuiEntry> compelteEList = this.getElements();
        int page = -1;
        for (int i = 0; i < compelteEList.size(); i++) {
            if (i % 28 == 0) page++;
        }

        return page;
    }
}
