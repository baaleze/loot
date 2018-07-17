package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;

public class Or implements Token {

    private final Token[] tokens;
    private final int[] chances;

    public Or(int[] chances, Token... tokens) {
        if (chances.length != tokens.length) {
            throw new IllegalArgumentException("there should be one proba for one token");
        }
        this.chances = chances;
        this.tokens = tokens;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen, Price p) {
        // choose one at random
        return this.tokens[LootGen.randomIndexWithChancesArray(this.chances)].gen(item, masculine, plural, vowel,
            lootGen, p);
    }

}
