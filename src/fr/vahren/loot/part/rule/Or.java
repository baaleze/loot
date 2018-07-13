package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;

public class Or implements Token {

    private final Token[] tokens;

    public Or(Token... tokens) {
        this.tokens = tokens;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel) {
        // choose one at random
        return this.tokens[LootGen.random(0, this.tokens.length - 1)].gen(item, masculine, plural, vowel);
    }

}
