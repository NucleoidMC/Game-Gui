package fr.catcore.gamegui.builder.gamebuilder;

import java.util.ArrayList;
import java.util.List;

public class CodecGuiBuilder {

    final List<CodecGuiEntry> elements = new ArrayList<>();

    public void add(CodecGuiEntry element) {
        this.elements.add(element);
    }

    public List<CodecGuiEntry> getElements() {
        List<CodecGuiEntry> newList = new ArrayList<CodecGuiEntry>();
        newList.addAll(this.elements);
        return newList;
    }
}
