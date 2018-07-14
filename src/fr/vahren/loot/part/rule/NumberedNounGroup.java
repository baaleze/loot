package fr.vahren.loot.part.rule;

import java.util.List;

import fr.vahren.loot.Item;
import fr.vahren.loot.part.terminal.Noun;

public class NumberedNounGroup extends NounGroup {

	public NumberedNounGroup(StringToken prefix, StringToken suffix, Class<? extends Noun> type) {
		super(prefix, suffix, type, Boolean.TRUE);
	}

	@Override
	protected List<String> getTokens(Item item) {
		final List<String> tokens = super.getTokens(item);
		tokens.add(1, String.valueOf((int) Math.floor(Math.random() * 1000 + 2)));
		return tokens;
	}
}
