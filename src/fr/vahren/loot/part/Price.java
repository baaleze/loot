package fr.vahren.loot.part;

import java.util.LinkedList;
import java.util.List;

public class Price {

    List<Price> subPrices = new LinkedList<>();

    public double compute(double cumul) {
        double res = cumul;
        // additions first
        for (final Price p : this.subPrices) {
            if (p instanceof PriceItem) {
                final Operation op = ((PriceItem) p).op;
                if (op != Operation.MULT && op != Operation.DIV) {
                    res = p.compute(res);
                }
            } else {
                res = p.compute(res);
            }
        }
        // multiplications
        for (final Price p : this.subPrices) {
            if (p instanceof PriceItem) {
                final Operation op = ((PriceItem) p).op;
                if (op == Operation.MULT || op == Operation.DIV) {
                    res = p.compute(res);
                }
            }
        }
        return res;
    }

    public void addPrices(List<Price> prices) {
        for (final Price p : prices) {
            this.subPrices.add(p);
        }
    }

    public void addPrices(Price[] prices) {
        for (final Price p : prices) {
            this.subPrices.add(p);
        }
    }

    public void addPrice(Price p) {
        this.subPrices.add(p);
    }

    @Override
    public String toString() {
        return "" + this.subPrices;
    }

    public enum Operation {
        ADD("+"), MULT("*"), SUB("-"), DIV("/");

        public String symbol;

        private Operation(String symbol) {
            this.symbol = symbol;
        }

    }

}
