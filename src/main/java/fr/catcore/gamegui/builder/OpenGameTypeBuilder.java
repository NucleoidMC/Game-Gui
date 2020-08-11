package fr.catcore.gamegui.builder;

import java.util.ArrayList;
import java.util.List;

public class OpenGameTypeBuilder {
    final List<OpenGameTypeEntry> elements = new ArrayList();

    public OpenGameTypeBuilder() {
    }

    public OpenGameTypeBuilder add(OpenGameTypeEntry entry) {
        this.elements.add(entry);
        return this;
    }

    public OpenGameTypeEntry[] getElements() {
        return elements.toArray(new OpenGameTypeEntry[0]);
    }
}
