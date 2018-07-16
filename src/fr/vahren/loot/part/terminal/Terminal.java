package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Terminal {
    public List<String> tags = new LinkedList<>();
    public Map<String, Integer> stats = new HashMap<>();
    public List<String> powers = new LinkedList<>();

    public abstract String gen(Item item, boolean masculine, boolean plural);

    public abstract void parse(String s);

    protected void addStatsTagsAndPowers(Item item) {
        addStatsTagsAndPowers(item, 1);
    }

    protected void addStatsTagsAndPowers(Item item, double factor) {
        this.stats.forEach((name, value) -> item.addStat(name, (int) (value * factor)));
        this.powers.forEach(p -> item.addPower(p));
        this.tags.forEach(t -> item.addTag(t));
    }

}
