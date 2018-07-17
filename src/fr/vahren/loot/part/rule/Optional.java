package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;

public class Optional implements Token {

    private final Token token;
    private final int chance;

    public Optional(Token t, int chance) {
        this.token = t;
        this.chance = chance;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen, Price p) {
        if (Math.random() < (this.chance / 100.0)) {
            final Price price = new Price();
            p.addPrice(price);
            return this.token.gen(item, masculine, plural, vowel, lootGen, price);

        } else {
            return "";
        }
    }

}
