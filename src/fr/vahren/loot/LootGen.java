package fr.vahren.loot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.vahren.loot.part.Price;
import fr.vahren.loot.part.rule.NounGroup;
import fr.vahren.loot.part.rule.NumberedNounGroup;
import fr.vahren.loot.part.rule.Optional;
import fr.vahren.loot.part.rule.Or;
import fr.vahren.loot.part.rule.Sequence;
import fr.vahren.loot.part.rule.StringToken;
import fr.vahren.loot.part.rule.Token;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Gear;
import fr.vahren.loot.part.terminal.Material;
import fr.vahren.loot.part.terminal.Modifier;
import fr.vahren.loot.part.terminal.Noun;
import fr.vahren.loot.part.terminal.Qualificatif;
import fr.vahren.loot.part.terminal.Terminal;
import fr.vahren.loot.part.terminal.TerminalGenerator;
import rita.RiMarkov;

public class LootGen {

	// default global instance
	public static final LootGen gen = new LootGen();

	public Map<Class<? extends Terminal>, TerminalGenerator<? extends Terminal>> generators;
	public RiMarkov uniqueGen;

	public static void main(String[] args) {
		gen.defaultTree();
		int c = 0;
		final Path path = Paths.get("lootResults.json");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write("[");
			final int nbLines = Integer.parseInt(args[0]);
			while (c < nbLines) {
				writer.write("\"");
				final String item = gen.gen().toString().replaceAll("\\s+", " ").replaceAll("' ", "'").replaceAll("\"",
						"'");
				writer.write(item);
				if (c != nbLines - 1) {
					writer.write("\",\n");
				} else {
					writer.write("\"\n");
				}
				c++;
			}
			writer.write("]");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public LootGen() {
		this("txt/gear.txt", "txt/nouns.txt", "txt/adjectives.txt", "txt/qualif.txt", "txt/modifier.txt",
				"txt/material.txt", "txt/names.txt");
	}

	public LootGen(List<String> gear, List<String> noun, List<String> adj, List<String> qualif, List<String> modifier,
			List<String> material, List<String> names) {
		this.generators = new HashMap<>();
		try {
			this.initGenerator(Gear.class, gear);
			this.initGenerator(Noun.class, noun);
			this.initGenerator(Adjective.class, adj);
			this.initGenerator(Qualificatif.class, qualif);
			this.initGenerator(Modifier.class, modifier);
			this.initGenerator(Material.class, material);
			this.loadUniqueNames(names);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public LootGen(String gear, String noun, String adj, String qualif, String modifier, String material,
			String names) {
		this.generators = new HashMap<>();
		try {
			this.initGenerator(Gear.class, gear);
			this.initGenerator(Noun.class, noun);
			this.initGenerator(Adjective.class, adj);
			this.initGenerator(Qualificatif.class, qualif);
			this.initGenerator(Modifier.class, modifier);
			this.initGenerator(Material.class, material);
			this.loadUniqueNames(names);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void initGenerator(Class<? extends Terminal> c, String path)
			throws InstantiationException, IllegalAccessException, IOException {
		final List<String> allLines = this.readAllLines(path);
		this.generators.put(c, new TerminalGenerator<>(allLines, c));
	}

	private void initGenerator(Class<? extends Terminal> c, List<String> allLines)
			throws InstantiationException, IllegalAccessException {
		this.generators.put(c, new TerminalGenerator<>(allLines, c));
	}

	private List<String> readAllLines(String path) throws FileNotFoundException, IOException {
		final List<String> allLines = new LinkedList<>();
		final File f = new File(path);
		final BufferedReader b = new BufferedReader(new FileReader(f));
		String readLine = "";
		while ((readLine = b.readLine()) != null) {
			allLines.add(readLine);
		}
		b.close();
		return allLines;
	}

	public void defaultTree() {
		this.start = new Sequence(new NounGroup(null, null, Gear.class, Boolean.FALSE, 20, 20), new Optional(new Or(
				new int[] { 70, 30 },
				new NounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, null, 0, 5),
				new NumberedNounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, 0, 5)),
				40));
	}

	private void loadUniqueNames(String path) throws IOException {
		final List<String> names = this.readAllLines(path);
		this.loadUniqueNames(names);
	}

	private void loadUniqueNames(final List<String> names) {
		// unique name generator markov chain
		this.uniqueGen = new RiMarkov(3, false, true);
		final List<Character> tokens = new LinkedList<>();
		for (final String name : names) {
			for (final char c : name.toLowerCase().toCharArray()) {
				tokens.add(c);
			}
		}
		final char[] tokensArray = new char[tokens.size()];
		int i = 0;
		for (final Character c : tokens) {
			tokensArray[i++] = c;
		}
		this.uniqueGen.loadTokens(tokensArray);
	}

	public Token start;

	public Item gen() {
		final Item item = new Item();
		final Price p = new Price();
		final String res = this.start.gen(item, false, false, false, this, p).trim();
		item.name = res.substring(0, 1).toUpperCase() + res.substring(1);
		item.price = p;
		item.checkUnique(this);
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

	public static int randomBetween(int... values) {
		return values[(int) Math.floor(Math.random() * values.length)];
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

	public static String join(String delimiter, String[] generateTokens) {
		return join(delimiter, Arrays.asList(generateTokens));
	}

	public static String join(String delimiter, List<String> strings) {
		final StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (final String s : strings) {
			buffer.append(s);
			if (i < strings.size() - 1) {
				buffer.append(delimiter);
			}
			i++;
		}
		return buffer.toString();
	}

	public <T extends Terminal> TerminalGenerator<T> getGenerator(Class<T> c) {
		return (TerminalGenerator<T>) this.generators.get(c);
	}

	public String generateUniqueName() {
		String join = LootGen.join("", this.uniqueGen.generateTokens(LootGen.random(5, 12)));
		// capitalize
		join = join.substring(0, 1).toUpperCase() + join.substring(1);
		return join;
	}

}
