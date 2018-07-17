package fr.vahren.loot.part;

public class PriceItem extends Price {

    protected double value;
    protected Operation op;

    public PriceItem(Operation op, double value) {
        this.op = op;
        this.value = value;
    }

    public PriceItem(String string) {
        final String first = string.substring(0, 1);
        switch (first) {
            case "+":
                this.op = Operation.ADD;
                break;
            case "-":
                this.op = Operation.SUB;
                break;
            case "*":
                this.op = Operation.MULT;
                break;
            case "/":
                this.op = Operation.DIV;
                break;
            default:
                this.op = Operation.ADD;
        }
        this.value = Double.parseDouble(string.substring(1));
    }

    @Override
    public double compute(double cumul) {
        switch (this.op) {
            case ADD:
                return cumul + this.value;
            case MULT:
                return cumul * this.value;
            case SUB:
                return cumul - this.value;
            case DIV:
                return cumul / this.value;
            default:
                return cumul + this.value;
        }
    }

    @Override
    public String toString() {
        return this.op.symbol + this.value;
    }

}
