package fr.vahren.loot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Item {

    private static final int UNIQUE_THRESHOLD = 8;
    public String name;
    public String uniqueName;
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
        final StringBuffer sb = new StringBuffer();
        if (this.uniqueName != null) {
            sb.append("***").append(this.uniqueName).append("***\\n");
        }
        sb.append("**")
            .append(this.name)
            .append("**\\n");
        if (!this.stats.isEmpty()) {
            sb.append(String.join(" | ",
                this.stats.keySet().stream().map(s -> {
                    final String statString =
                        this.stats.get(s) > 0 ? "+" + this.stats.get(s) : this.stats.get(s).toString();
                    if (s.endsWith("%")) {
                        return s.substring(0, s.length() - 1) + " = " + statString + "%";
                    } else {
                        return s + " = " + statString;
                    }
                }).collect(Collectors.toList())))
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

    public void checkUnique() {
        if (this.stats.size() + this.powers.size() + this.tags.size() > UNIQUE_THRESHOLD) {
            this.uniqueName = LootGen.generateUniqueName();
        }
    }

}
