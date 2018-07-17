package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.PriceItem;
import fr.vahren.loot.part.rule.Token;
import java.util.Arrays;

public class Adjective extends Terminal {

    public boolean goesBefore;
    public String feminineSingular;
    public String femininePlural;
    public String masculineSingular;
    public String mascuinePlural;
    private Modifier mod;

    @Override
    public String gen(Item item, boolean masculine, boolean plural, LootGen lootGen) {

        if (this.mod != null) {
            this.addStatsTagsAndPowers(item, this.mod.factor);
            return this.mod.gen(item, masculine, plural, lootGen) + " "
                + this.get(masculine, plural);
        } else {
            this.addStatsTagsAndPowers(item);
            return this.get(masculine, plural);
        }
    }

    private String get(boolean masculine, boolean plural) {
        return masculine
            ? plural ? this.mascuinePlural : this.masculineSingular
            : plural ? this.femininePlural : this.feminineSingular;
    }

    @Override
    public void parse(String s) {
        final String[] split = s.split("\\.", 9);
        if (split.length != 9) {
            throw new IllegalArgumentException(s + " is not a valid Adjective");
        }
        this.goesBefore = "B".equals(split[0]);
        // singular/plural
        this.masculineSingular = split[1];
        this.mascuinePlural = split[2];
        this.feminineSingular = split[3];
        this.femininePlural = split[4];
        // stat
        for (final String stat : split[5].split("\\|")) {
            if (stat.contains("=")) {
                final String[] statSplit = stat.split("=");
                this.stats.put(statSplit[0], Integer.parseInt(statSplit[1]));
            }
        }
        // tag
        this.tags.addAll(Arrays.asList(split[6].split("\\|")));

        // power
        this.powers.addAll(Arrays.asList(split[7].split("\\|")));

        // price
        this.price = new PriceItem(split[8]);
    }

    public boolean beginsWithVowel(LootGen lootGen) {
        if (Math.random() < 0.3) {
            this.mod = lootGen.getGenerator(Modifier.class).gen();
            return Token.vowels.contains(this.mod.value.substring(0, 1));
        } else {
            this.mod = null;
            return Token.vowels.contains(this.mascuinePlural.substring(0, 1));
        }
    }

}
