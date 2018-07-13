package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Noun;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NounGroup implements Token {

    private static final String vowels = "aeiouyéèàêôîâû";
    public StringToken prefix;
    public StringToken suffix;
    private final int nbAdj;
    private final Class<? extends Noun> type;

    public NounGroup(StringToken prefix,
        StringToken suffix, Class<? extends Noun> type) {
        this.nbAdj = LootGen.random(0, 3);
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
    }

    @Override
    public String gen(Item item, boolean dontcare, boolean osef, boolean ahah) {
        // get noun
        final Noun actualNoun = LootGen.getGenerator(this.type).gen();

        // get gender and plurality
        final boolean masculine = actualNoun.masculine;
        final boolean plural = Math.random() > 0.5;
        boolean vowel = vowels.contains(actualNoun.get(plural).substring(0, 1));

        // generate strings
        final List<Adjective> adjBef = new LinkedList<>();
        final List<Adjective> adjAft = new LinkedList<>();
        Adjective a;
        for (int i = 0; i < this.nbAdj; i++) {
            a = LootGen.getGenerator(Adjective.class).gen();
            if (a.goesBefore) {
                adjBef.add(a);
            } else {
                adjAft.add(a);
            }
        }
        if (!adjBef.isEmpty()) {
            vowel = vowels.contains(adjBef.get(0).masculineSingular.substring(0, 1));
        }

        final String n = actualNoun.gen(item, masculine, plural);

        final String pref = this.prefix != null ? this.prefix.gen(item, masculine, plural, vowel) : "";
        final String suff = this.suffix != null ? this.suffix.gen(item, masculine, plural, vowel) : "";

        final CharSequence adjAftStr =
            String.join(" ", adjAft.stream().map(ad -> ad.gen(item, masculine, plural)).collect(Collectors.toList()));
        final CharSequence adjBefStr =
            String.join(" ", adjBef.stream().map(ad -> ad.gen(item, masculine, plural)).collect(Collectors.toList()));

        return String.join(" ", pref, adjBefStr, n, adjAftStr, suff);
    }

}
