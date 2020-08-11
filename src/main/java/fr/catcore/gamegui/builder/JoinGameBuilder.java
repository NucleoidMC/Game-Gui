package fr.catcore.gamegui.builder;

import java.util.ArrayList;
import java.util.List;

public class JoinGameBuilder {
    final List<JoinGameEntry> elements = new ArrayList();

    public JoinGameBuilder() {
    }

    public JoinGameBuilder add(JoinGameEntry entry) {
        this.elements.add(entry);
        return this;
    }

    public JoinGameEntry[] getElements() {
        return elements.toArray(new JoinGameEntry[0]);
    }
}
