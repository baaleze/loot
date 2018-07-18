package fr.vahren.loot.part.rule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;
import fr.vahren.loot.part.Price;
import fr.vahren.loot.part.terminal.Adjective;
import fr.vahren.loot.part.terminal.Noun;
import fr.vahren.loot.part.terminal.Qualificatif;

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
	public String gen(Item item, boolean masculine, boolean plural, boolean vowel, LootGen lootGen, Price p) {
		this.type = Noun.class;
		this.nbAdj = LootGen.randomBetween(0, 0, 0, 1, 1, 1, 2, 2);
		return "\"" + this.getToken(item, masculine, plural, lootGen, p).trim() + "\"";
	}

	protected String getToken(Item item, boolean masculineParent, boolean pluralParent, LootGen lootGen, Price p) {
		// get noun
		final Noun actualNoun = lootGen.getGenerator(this.type).gen();
		p.addPrice(actualNoun.price);

		// get gender and plurality
		final boolean masculine = actualNoun.masculine;
		final boolean plural = Math.random() > 0.5;
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
			p.addPrice(a.price);
		}
		if (!adjBef.isEmpty()) {
			vowel = vowels.contains(adjBef.get(0).masculineSingular.substring(0, 1));
		}

		final Qualificatif qualif = lootGen.getGenerator(Qualificatif.class).gen();
		final String q = qualif.gen(item, masculineParent, pluralParent, lootGen);
		p.addPrice(qualif.price);

		final String n = actualNoun.gen(item, masculine, plural, lootGen);

		final String pref = this.pref.gen(item, masculine, plural, vowel, lootGen, p);

		final String adjAftStr = String.join(" ", this.map(item, lootGen, masculine, plural, adjAft));
		final String adjBefStr = String.join(" ", this.map(item, lootGen, masculine, plural, adjBef));

		final List<String> tokens = new LinkedList<>();
		Collections.addAll(tokens, q, pref, adjBefStr, n, adjAftStr);
		return String.join(" ", tokens);
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
