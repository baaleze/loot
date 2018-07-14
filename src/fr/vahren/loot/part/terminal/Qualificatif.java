package fr.vahren.loot.part.terminal;

import java.util.Arrays;

import fr.vahren.loot.Item;

public class Qualificatif extends Terminal {

	public String feminineSingular;
	public String femininePlural;
	public String masculineSingular;
	public String mascuinePlural;

	@Override
	public String gen(Item item, boolean masculine, boolean plural) {
		this.addStatsTagsAndPowers(item);
		return " " + this.get(masculine, plural);
	}

	private String get(boolean masculine, boolean plural) {
		return masculine ? plural ? this.mascuinePlural : this.masculineSingular
				: plural ? this.femininePlural : this.feminineSingular;
	}

	@Override
	public void parse(String s) {
		final String[] split = s.split("\\.", 7);
		if (split.length != 7) {
			throw new IllegalArgumentException(s + " is not a valid Qualificatif");
		}
		// singular/plural
		this.masculineSingular = split[0];
		this.mascuinePlural = split[1];
		this.feminineSingular = split[2];
		this.femininePlural = split[3];
		// stat
		for (final String stat : split[4].split("\\|")) {
			if (stat.contains("=")) {
				final String[] statSplit = stat.split("=");
				this.stats.put(statSplit[0], Integer.parseInt(statSplit[1]));
			}
		}
		// tag
		this.tags.addAll(Arrays.asList(split[5].split("\\|")));

		// power
		this.powers.addAll(Arrays.asList(split[6].split("\\|")));
	}

}
