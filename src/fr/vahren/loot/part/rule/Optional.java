package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;

public class Optional implements Token {

    private final Token token;
    private final int chance;

    public Optional(Token t, int chance) {
        this.token = t;
        this.chance = chance;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel) {
        if (Math.random() < (this.chance / 100.0)) {
            return this.token.gen(item, masculine, plural, vowel);
        } else {
            return "";
        }
    }

}
