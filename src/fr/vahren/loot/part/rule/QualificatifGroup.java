package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Gear;
import fr.vahren.loot.part.terminal.Noun;
import fr.vahren.loot.part.terminal.Qualificatif;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * QUALIF de NOUN/GEAR
 *
 * @author baaleze
 *
 */
public class QualificatifGroup implements Token {

    private Class<? extends Noun> type;
    private int nbAdj;
    private final StringToken pref;

    public QualificatifGroup() {
        this.pref = new StringToken("de la", "des", "du", "des", "de l'");
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel) {
        this.type = LootGen.random(0, 100) < 50 ? Gear.class : Noun.class;
        this.nbAdj = LootGen.randomBetween(0, 0, 0, 1, 1, 1, 2, 2);
        return "\"" + this.getToken(item, masculine, plural).trim() + "\"";
    }

    protected String getToken(Item item, boolean masculineParent, boolean pluralParent) {
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

        final String q = LootGen.getGenerator(Qualificatif.class).gen().gen(item, masculineParent, pluralParent);

        final String n = actualNoun.gen(item, masculine, plural);

        final String pref = this.pref.gen(item, masculine, plural, vowel);

        final String adjAftStr = String.join(" ",
            adjAft.stream().map(ad -> ad.gen(item, masculine, plural)).collect(Collectors.toList()));
        final String adjBefStr = String.join(" ",
            adjBef.stream().map(ad -> ad.gen(item, masculine, plural)).collect(Collectors.toList()));

        final List<String> tokens = new LinkedList<>();
        Collections.addAll(tokens, q, pref, adjBefStr, n, adjAftStr);
        return String.join(" ", tokens);
    }

}
