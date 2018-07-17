package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.rule.Token;
import java.util.Arrays;

public class Noun extends Terminal {

    public boolean masculine;
    public String singular;
    public String plural;

    public String get(boolean plural) {
        return plural ? this.plural : this.singular;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, LootGen lootGen) {
        addStatsTagsAndPowers(item);
        return get(plural);
    }

    /**
     * Format is <code>[MF].singularform.pluralform.stat1=statvalue1|stat2=statvalue2.tag1|tag2|tag3.power 1|another power\n</code>
     */
    @Override
    public void parse(String s) {
        final String[] split = s.split("\\.", 6);
        if (split.length != 6) {
            throw new IllegalArgumentException(s + " is not a valid Noun");
        }
        // gender
        this.masculine = "M".equals(split[0]);
        // singular/plural
        this.singular = split[1];
        this.plural = split[2];
        // stat
        final String[] stats = split[3].split("\\|");
        for (final String stat : stats) {
            if (stat.contains("=")) {
                final String[] statSplit = stat.split("=");
                this.stats.put(statSplit[0], Integer.parseInt(statSplit[1]));
            }
        }
        // tag
        this.tags.addAll(Arrays.asList(split[4].split("\\|")));

        // power
        this.powers.addAll(Arrays.asList(split[5].split("\\|")));
    }

    public boolean beginsWithVowel() {
        return Token.vowels.contains(this.plural.substring(0, 1));
    }

}
