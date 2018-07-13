package fr.vahren.loot.part.rule;

import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.terminal.Terminal;

public class TerminalToken<T extends Terminal> {

    public final Class<T> terminal;

    public TerminalToken(Class<T> c) {
        this.terminal = c;
    }

    public T gen() {
        return (T) LootGen.generators.get(this.terminal).gen();
    }

}
