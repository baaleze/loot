package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import java.util.Arrays;

public class Material extends Terminal {

    private String material;

    @Override
    public String gen(Item item, boolean masculine, boolean plural) {
        addStatsTagsAndPowers(item);
        return this.material;
    }

    @Override
    public void parse(String s) {
        final String[] split = s.split("\\.", 4);
        if (split.length != 4) {
            throw new IllegalArgumentException(s + " is not a valid Material");
        }
        this.material = split[0];
        // stat
        for (final String stat : split[1].split("\\|")) {
            if (stat.contains("=")) {
                final String[] statSplit = stat.split("=");
                this.stats.put(statSplit[0], Integer.parseInt(statSplit[1]));
            }
        }
        // tag
        this.tags.addAll(Arrays.asList(split[2].split("\\|")));

        // power
        this.powers.addAll(Arrays.asList(split[3].split("\\|")));
    }

}
