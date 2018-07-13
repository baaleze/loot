package fr.vahren.loot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Item {

    public String name;
    public Map<String, Integer> stats = new HashMap<>();
    public List<String> powers = new LinkedList<>();
    public List<String> tags = new LinkedList<>();

    public void addStat(String stat, int value) {
        if (this.stats.containsKey(stat)) {
            this.stats.put(stat, this.stats.get(stat) + value);
        } else {
            this.stats.put(stat, value);
        }
    }

    public void addTag(String tag) {
        if (tag != null && !"".equals(tag.trim()) && !this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void addPower(String power) {
        if (power != null && !"".equals(power.trim()) && !this.powers.contains(power)) {
            this.powers.add(power);
        }
    }

    @Override
    public String toString() {
        return "Item [name=" + this.name + ", stats=" + this.stats + ", powers=" + this.powers + ", tags=" + this.tags
            + "]";
    }

}
