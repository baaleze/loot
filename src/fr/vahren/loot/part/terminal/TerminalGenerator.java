package fr.vahren.loot.part.terminal;

import java.util.LinkedList;
import java.util.List;

public class TerminalGenerator<T extends Terminal> {

    List<T> pool;

    public TerminalGenerator(List<T> items) {
        this.pool = new LinkedList<>(items);
    }

    public TerminalGenerator(List<String> items, Class<T> c)
        throws InstantiationException, IllegalAccessException {
        this.pool = new LinkedList<>();
        for (final String item : items) {
            final T t = c.newInstance();
            t.parse(item);
            this.pool.add(t);
        }
    }

    public T gen() {
        return this.pool.get((int) Math.floor(Math.random() * this.pool.size()));
    }

}
