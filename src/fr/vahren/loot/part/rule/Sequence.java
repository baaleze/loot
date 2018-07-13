package fr.vahren.loot.part.rule;

import fr.vahren.loot.Item;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Sequence implements Token {

    private final Token[] tokens;

    public Sequence(Token... tokens) {
        this.tokens = tokens;
    }

    @Override
    public String gen(Item item, boolean masculine, boolean plural, boolean vowel) {
        return String.join(" ", Arrays.asList(this.tokens).stream().map(t -> t.gen(item, masculine, plural, vowel))
            .collect(Collectors.toList()));
    }

}
