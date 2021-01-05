package fr.catcore.gamegui.builder;

import java.util.ArrayList;
import java.util.List;

public class GuiBuilder<E extends GuiEntry> {

    final List<E> elements = new ArrayList<>();

    public void add(E element) {
        this.elements.add(element);
    }

    public List<E> getElements() {
        List<E> newList = new ArrayList<E>();
        newList.addAll(this.elements);
        return newList;
    }
}
