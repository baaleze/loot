package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class Terminal {
    public List<String> tags = new LinkedList<>();
    public Map<String, Integer> stats = new HashMap<>();
    public List<String> powers = new LinkedList<>();

    public abstract String gen(Item item, boolean masculine, boolean plural, LootGen lootGen);

    public abstract void parse(String s);

    protected void addStatsTagsAndPowers(Item item) {
        addStatsTagsAndPowers(item, 1);
    }

    protected void addStatsTagsAndPowers(Item item, double factor) {
        for (final Entry<String, Integer> e : this.stats.entrySet()) {
            item.addStat(e.getKey(), (int) (e.getValue() * factor));
        }
        for (final String p : this.powers) {
            item.addPower(p);
        }
        for (final String t : this.tags) {
            item.addTag(t);
        }
    }

}
