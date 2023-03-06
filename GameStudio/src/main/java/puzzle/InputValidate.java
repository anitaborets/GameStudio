package puzzle;

public class InputValidate {
    private boolean myResult;
    private int colChoiceChar;
    private int colChoice;

    public InputValidate(int colChoiceChar) {
        this.colChoiceChar = colChoiceChar;
    }

    boolean is() {
        return myResult;
    }

    public int getColChoice() {
        return colChoice;
    }

    public InputValidate invoke() {
        if (colChoiceChar >= ((int) 'A') && colChoiceChar <= ((int) 'D')) {
            colChoice = colChoiceChar - ((int) 'A');
        } else if (colChoiceChar >= ((int) 'a') && colChoiceChar <= ((int) 'd')) {
            colChoice = colChoiceChar - ((int) 'a');
        } else {
            System.out.println("Unknown symbol. Try again.");
            myResult = true;
            return this;
        }
        myResult = false;
        return this;
    }
}
