package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import java.util.LinkedList;
import java.util.List;

public class Sequence implements Token {

    private final Token[] tokens;

    public Sequence(Token... tokens) {
        this.tokens = tokens;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen) {
        final List<String> sequence = new LinkedList<>();
        for (final Token t : this.tokens) {
            sequence.add(t.gen(item, masculine, plural, vowel, lootGen));
        }
        return LootGen.join(" ", sequence);
    }

}
