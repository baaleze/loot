package fr.vahren.loot.part.terminal;

import fr.vahren.loot.Item;

public class Modifier extends Terminal {

	private String value;

	@Override
	public String gen(Item item, boolean masculine, boolean plural) {
		return this.value;
	}

	@Override
	public void parse(String s) {
		this.value = s;
	}

}
