package fr.vahren.loot;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import fr.vahren.loot.part.rule.NounGroup;
import fr.vahren.loot.part.rule.NumberedNounGroup;
import fr.vahren.loot.part.rule.Optional;
import fr.vahren.loot.part.rule.Or;
import fr.vahren.loot.part.rule.Sequence;
import fr.vahren.loot.part.rule.StringToken;
import fr.vahren.loot.part.rule.Token;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Gear;
import fr.vahren.loot.part.terminal.Noun;
import fr.vahren.loot.part.terminal.Terminal;
import fr.vahren.loot.part.terminal.TerminalGenerator;

public class LootGen {

	public static Map<Class<? extends Terminal>, TerminalGenerator<? extends Terminal>> generators;

	public static void main(String[] args) {
		generators = new HashMap<>();
		try {
			generators.put(Gear.class,
					new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/gear.txt")), Gear.class));
			generators.put(Noun.class,
					new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/nouns.txt")), Noun.class));
			generators.put(Adjective.class,
					new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/adjectives.txt")), Adjective.class));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		final LootGen gen = new LootGen();
		// the definition is done HERE
		gen.start = new Sequence(new NounGroup(null, null, Gear.class, null),
				new Optional(new Or(new int[] { 70, 30 },
						new NounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, null),
						new NumberedNounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class)),
						40));
		int c = 0;
		while (c < 10) {

			System.out.println(gen.gen().toString().replaceAll("\\s+", " "));
			c++;
		}
	}

	private Token start;

	public Item gen() {
		final Item item = new Item();
		final String res = this.start.gen(item, false, false, false);
		item.name = res;
		return item;
	}

	/**
	 * Random in from 'from' to 'to' INCLUSIVE
	 *
	 * @param from
	 * @param to
	 * @return
	 */
	public static int random(int from, int to) {
		return (int) Math.floor(Math.random() * (to - from + 1)) + from;
	}

	public static int randomIndexWithChancesArray(int[] chances) {
		final int percent = LootGen.random(0, 100);
		int cumul = 0;
		for (int i = 0; i < chances.length; i++) {
			cumul += chances[i];
			if (percent < cumul) {
				return i;
			}
		}
		return 0;
	}

	public static <T extends Terminal> TerminalGenerator<T> getGenerator(Class<T> c) {
		return (TerminalGenerator<T>) generators.get(c);
	}

}
