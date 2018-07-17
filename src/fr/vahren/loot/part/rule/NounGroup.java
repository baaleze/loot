package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Material;
import fr.vahren.loot.part.terminal.Noun;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NounGroup implements Token {

    public StringToken prefix;
    public StringToken suffix;
    protected int nbAdj = 0;
    protected final Class<? extends Noun> type;
    protected final Boolean plural;
    private final int qualifChance;
    private final int materialChance;

    public NounGroup(StringToken prefix, StringToken suffix, Class<? extends Noun> type, Boolean plural,
        int qualifChance, int materialChance) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
        this.plural = plural;
        this.qualifChance = qualifChance;
        this.materialChance = materialChance;
    }

    @Override
    public String gen(Item item, boolean dontcare, boolean osef, boolean ahah, LootGen lootGen) {
        this.nbAdj = LootGen.randomBetween(0, 0, 1, 1, 1, 2, 2, 3);
        return String.join(" ", this.getTokens(item, lootGen));
    }

    protected List<String> getTokens(Item item, LootGen lootGen) {
        // get noun
        final Noun actualNoun = lootGen.getGenerator(this.type).gen();

        // get gender and plurality
        final boolean masculine = actualNoun.masculine;
        final boolean plural = this.plural != null ? this.plural.booleanValue() : Math.random() > 0.5;
        boolean vowel = vowels.contains(actualNoun.get(plural).substring(0, 1));

        // generate strings
        final List<Adjective> adjBef = new LinkedList<>();
        final List<Adjective> adjAft = new LinkedList<>();
        Adjective a;
        for (int i = 0; i < this.nbAdj; i++) {
            a = lootGen.getGenerator(Adjective.class).gen();
            if (a.goesBefore) {
                adjBef.add(a);
            } else {
                adjAft.add(a);
            }
        }
        if (!adjBef.isEmpty()) {
            vowel = vowels.contains(adjBef.get(0).masculineSingular.substring(0, 1));
        }

        final String n = actualNoun.gen(item, masculine, plural, lootGen);

        final String pref = this.prefix != null ? this.prefix.gen(item, masculine, plural, vowel, lootGen) : "";
        final String suff = this.suffix != null ? this.suffix.gen(item, masculine, plural, vowel, lootGen) : "";

        final String adjAftStr = LootGen.join(" ", map(item, lootGen, masculine, plural, adjAft));
        final String adjBefStr = LootGen.join(" ", map(item, lootGen, masculine, plural, adjBef));

        final List<String> tokens = new LinkedList<>();
        Collections.addAll(tokens, pref, adjBefStr, n, adjAftStr, suff);

        // qualificatif
        if (LootGen.random(0, 100) < this.qualifChance) {
            tokens.add(new QualificatifGroup().gen(item, masculine, plural, vowel, lootGen));
        }
        // material
        if (LootGen.random(0, 100) < this.materialChance) {
            // add material
            tokens.add(4, lootGen.getGenerator(Material.class).gen().gen(item, masculine, plural, lootGen));
        }

        return tokens;
    }

    private List<String> map(Item item, LootGen lootGen, final boolean masculine, final boolean plural,
        final List<Adjective> adjAft) {
        final List<String> s = new LinkedList<>();
        for (final Adjective a : adjAft) {
            s.add(a.gen(item, masculine, plural, lootGen));
        }
        return s;
    }
}
