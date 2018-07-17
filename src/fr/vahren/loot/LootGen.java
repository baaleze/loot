package fr.vahren.loot;

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
import rita.RiMarkov;

public class LootGen {

    // default global instance
    public static final LootGen gen = defaultLootGenerator();

    public Map<Class<? extends Terminal>, TerminalGenerator<? extends Terminal>> generators;
    public RiMarkov uniqueGen;

    public static void main(String[] args) {
        int c = 0;
        final Path path = Paths.get("lootResults.json");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("[");
            final int nbLines = Integer.parseInt(args[0]);
            while (c < nbLines) {
                writer.write("\"");
                writer.write(gen.gen().toString().replaceAll("\\s+", " ").replaceAll("' ", "'").replaceAll("\"", "'"));
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
        this.generators = new HashMap<>();
        try {
            initGenerator(Gear.class, "txt/gear.txt");
            initGenerator(Noun.class, "txt/nouns.txt");
            initGenerator(Adjective.class, "txt/adjectives.txt");
            initGenerator(Qualificatif.class, "txt/qualif.txt");
            initGenerator(Modifier.class, "txt/modifier.txt");
            initGenerator(Material.class, "txt/material.txt");
            loadUniqueNames();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void initGenerator(Class<? extends Terminal> c, String path)
        throws InstantiationException, IllegalAccessException, IOException {
        final List<String> allLines = readAllLines(path);
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

    public static LootGen defaultLootGenerator() {
        final LootGen l = new LootGen();
        l.start = new Sequence(new NounGroup(null, null, Gear.class, Boolean.FALSE, 20, 20), new Optional(new Or(
            new int[] { 70, 30 },
            new NounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, null, 0, 5),
            new NumberedNounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, 0, 5)),
            40));
        return l;
    }

    private void loadUniqueNames() throws IOException {
        // unique name generator markov chain
        this.uniqueGen = new RiMarkov(3, false, true);
        final List<Character> tokens = new LinkedList<>();
        final List<String> names = Files.readAllLines(Paths.get("txt/names.txt"));
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

    private Token start;

    public Item gen() {
        final Item item = new Item();
        final String res = this.start.gen(item, false, false, false, this).trim();
        item.name = res.substring(0, 1).toUpperCase() + res.substring(1);
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
