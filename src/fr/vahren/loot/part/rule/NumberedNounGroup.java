package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;
import fr.vahren.loot.part.terminal.Noun;
import java.util.List;

public class NumberedNounGroup extends NounGroup {

    public NumberedNounGroup(StringToken prefix, StringToken suffix, Class<? extends Noun> type, int qualifChance,
        int materialChance) {
        super(prefix, suffix, type, Boolean.TRUE, qualifChance, materialChance);
    }

    @Override
    protected List<String> getTokens(Item item, LootGen lootGen, Price p) {
        final List<String> tokens = super.getTokens(item, lootGen, p);
        tokens.add(1, String.valueOf((int) Math.floor(Math.pow((Math.random() * 15 + 1), 2))));
        return tokens;
    }
}
