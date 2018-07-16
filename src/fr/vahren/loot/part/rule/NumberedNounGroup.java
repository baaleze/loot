package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.part.terminal.Noun;
import java.util.List;

public class NumberedNounGroup extends NounGroup {

    public NumberedNounGroup(StringToken prefix, StringToken suffix, Class<? extends Noun> type, int qualifChance,
        int materialChance) {
        super(prefix, suffix, type, Boolean.TRUE, qualifChance, materialChance);
    }

    @Override
    protected List<String> getTokens(Item item) {
        final List<String> tokens = super.getTokens(item);
        tokens.add(1, String.valueOf((int) Math.floor(Math.random() * 1000 + 2)));
        return tokens;
    }
}
