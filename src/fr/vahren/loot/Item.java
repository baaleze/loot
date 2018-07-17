package fr.vahren.loot;

import fr.vahren.loot.part.Price;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Item {

    private static final int UNIQUE_THRESHOLD = 8;
    public String name;
    public String uniqueName;
    public Map<String, Integer> stats = new HashMap<>();
    public List<String> powers = new LinkedList<>();
    public List<String> tags = new LinkedList<>();
    public Price price;

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
        final StringBuffer sb = new StringBuffer();
        if (this.uniqueName != null) {
            sb.append("***").append(this.uniqueName).append("***\\n");
        }
        sb.append("**")
            .append(this.name)
            .append("**\\n");
        if (!this.stats.isEmpty()) {
            final List<String> statStrings = new LinkedList<>();
            for (final String s : this.stats.keySet()) {
                final String statString =
                    this.stats.get(s) > 0 ? "+" + this.stats.get(s) : this.stats.get(s).toString();
                if (s.endsWith("%")) {
                    statStrings.add(s.substring(0, s.length() - 1) + " = " + statString + "%");
                } else {
                    statStrings.add(s + " = " + statString);
                }
            }
            sb.append(LootGen.join(" | ", statStrings))
                .append("\\n");
        }
        if (!this.powers.isEmpty()) {
            sb.append("*").append(String.join("; ", this.powers)).append("*\\n");
        }
        if (!this.tags.isEmpty()) {
            sb.append("[").append(String.join("|", this.tags)).append("]");
        }

        return sb.toString();
    }

    public void checkUnique(LootGen lootGen) {
        if (this.stats.size() + this.powers.size() + this.tags.size() > UNIQUE_THRESHOLD) {
            this.uniqueName = lootGen.generateUniqueName();
        }
    }

}
