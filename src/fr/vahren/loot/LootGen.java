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
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import rita.RiMarkov;

public class LootGen {

    public static Map<Class<? extends Terminal>, TerminalGenerator<? extends Terminal>> generators;
    public static RiMarkov uniqueGen;

    public static void main(String[] args) {
        generators = new HashMap<>();
        try {
            generators.put(Gear.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/gear.txt")), Gear.class));
            generators.put(Noun.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/nouns.txt")), Noun.class));
            generators.put(Adjective.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/adjectives.txt")), Adjective.class));
            generators.put(Qualificatif.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/qualif.txt")), Qualificatif.class));
            generators.put(Modifier.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/modifier.txt")), Modifier.class));
            generators.put(Material.class,
                new TerminalGenerator<>(Files.readAllLines(Paths.get("txt/material.txt")), Material.class));
            loadUniqueNames();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final LootGen gen = new LootGen();
        // the definition is done HERE
        gen.start = new Sequence(new NounGroup(null, null, Gear.class, Boolean.FALSE, 20, 20), new Optional(new Or(
            new int[] { 70, 30 },
            new NounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, null, 0, 5),
            new NumberedNounGroup(new StringToken("de la", "des", "du", "des", "de l'"), null, Noun.class, 0, 5)),
            40));
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

    private static void loadUniqueNames() throws IOException {
        // unique name generator markov chain
        uniqueGen = new RiMarkov(3, false, true);
        final List<Character> tokens = new LinkedList<>();
        Files.readAllLines(Paths.get("txt/names.txt")).forEach(name -> {
            for (final char c : name.toLowerCase().toCharArray()) {
                tokens.add(c);
            }
        });
        final char[] tokensArray = new char[tokens.size()];
        int i = 0;
        for (final Character c : tokens) {
            tokensArray[i++] = c;
        }
        uniqueGen.loadTokens(tokensArray);
    }

    private Token start;

    public Item gen() {
        final Item item = new Item();
        final String res = this.start.gen(item, false, false, false).trim();
        item.name = res.substring(0, 1).toUpperCase() + res.substring(1);
        item.checkUnique();
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

    public static <T extends Terminal> TerminalGenerator<T> getGenerator(Class<T> c) {
        return (TerminalGenerator<T>) generators.get(c);
    }

    public static String generateUniqueName() {
        String join = String.join("", uniqueGen.generateTokens(LootGen.random(5, 12)));
        // capitalize
        join = join.substring(0, 1).toUpperCase() + join.substring(1);
        return join;
    }

}
