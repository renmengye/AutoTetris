package autotetris.ai;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author rmy
 */
public class Example<TI, TO> implements Serializable {

    private List<TI> inputValues;
    private List<TO> expectedValues;

    public Example(List<TI> input, List<TO> expected) {
        inputValues = input;
        expectedValues = expected;
    }

    public Example(List<TI> input) {
        this(input, null);
    }

    public Example(TI... inputs) {
        this(Arrays.asList(inputs), null);
    }

    // <editor-fold defaultstate="collapsed" desc="Getter/Setter">
    /**
     * @return the inputValue
     */
    public List<TI> getInputValues() {
        return inputValues;
    }

    /**
     * @return the expectedValue
     */
    public List<TO> getExpectedValues() {
        return expectedValues;
    }
    // </editor-fold>
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Example:\ninput:\n");
        for (TI value : inputValues) {
            builder.append(value.toString());
            builder.append("\n");
        }
        builder.append("output:\n");

        for (TO value : expectedValues) {
            builder.append(value.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
