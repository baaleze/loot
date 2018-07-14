package fr.vahren.loot.part.terminal;

import java.util.Arrays;

import fr.vahren.loot.Item;
import fr.vahren.loot.LootGen;

public class Adjective extends Terminal {

	public boolean goesBefore;
	public String feminineSingular;
	public String femininePlural;
	public String masculineSingular;
	public String mascuinePlural;

	@Override
	public String gen(Item item, boolean masculine, boolean plural) {
		this.addStatsTagsAndPowers(item);
		if (Math.random() < 0.2) {
			return LootGen.getGenerator(Modifier.class).gen().gen(item, masculine, plural) + " "
					+ this.get(masculine, plural);
		} else {
			return this.get(masculine, plural);
		}
	}

	private String get(boolean masculine, boolean plural) {
		return masculine ? plural ? this.mascuinePlural : this.masculineSingular
				: plural ? this.femininePlural : this.feminineSingular;
	}

	@Override
	public void parse(String s) {
		final String[] split = s.split("\\.", 8);
		if (split.length != 8) {
			throw new IllegalArgumentException(s + " is not a valid Noun");
		}
		this.goesBefore = "B".equals(split[0]);
		// singular/plural
		this.masculineSingular = split[1];
		this.mascuinePlural = split[2];
		this.feminineSingular = split[3];
		this.femininePlural = split[4];
		// stat
		for (final String stat : split[5].split("\\|")) {
			if (stat.contains("=")) {
				final String[] statSplit = stat.split("=");
				this.stats.put(statSplit[0], Integer.parseInt(statSplit[1]));
			}
		}
		// tag
		this.tags.addAll(Arrays.asList(split[6].split("\\|")));

		// power
		this.powers.addAll(Arrays.asList(split[7].split("\\|")));
	}

}
