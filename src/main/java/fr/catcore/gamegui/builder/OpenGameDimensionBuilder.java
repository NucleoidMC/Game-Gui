package fr.catcore.gamegui.builder;

import java.util.ArrayList;
import java.util.List;

public class OpenGameDimensionBuilder {
    final List<OpenGameDimensionEntry> elements = new ArrayList();

    public OpenGameDimensionBuilder() {
    }

    public OpenGameDimensionBuilder add(OpenGameDimensionEntry entry) {
        this.elements.add(entry);
        return this;
    }

    public OpenGameDimensionEntry[] getElements() {
        return elements.toArray(new OpenGameDimensionEntry[0]);
    }
}
