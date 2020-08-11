package fr.catcore.gamegui.builder;

import java.util.ArrayList;
import java.util.List;

public class OpenConfiguredGameBuilder {
    final List<OpenConfiguredGameEntry> elements = new ArrayList();

    public OpenConfiguredGameBuilder() {
    }

    public OpenConfiguredGameBuilder add(OpenConfiguredGameEntry entry) {
        this.elements.add(entry);
        return this;
    }

    public OpenConfiguredGameEntry[] getElements() {
        return elements.toArray(new OpenConfiguredGameEntry[0]);
    }
}
