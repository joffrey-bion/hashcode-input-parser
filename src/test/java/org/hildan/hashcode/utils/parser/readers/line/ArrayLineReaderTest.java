package org.hildan.hashcode.utils.parser.readers.line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

import org.hildan.hashcode.utils.parser.context.Context;
import org.hildan.hashcode.utils.parser.config.Config;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class ArrayLineReaderTest {

    private static class Expectation<T> {

        private final IntFunction<T[]> arrayCreator;

        private final Function<String, T> converter;

        private final String line;

        private final T[] expectedOutput;

        private Expectation(IntFunction<T[]> arrayCreator, Function<String, T> converter, String line,
                T[] expectedOutput) {

            this.arrayCreator = arrayCreator;
            this.converter = converter;
            this.line = line;
            this.expectedOutput = expectedOutput;
        }
    }

    @DataPoints
    public static Expectation<?>[] conversions() {
        return new Expectation<?>[] { //
                new Expectation<>(Integer[]::new, Integer::parseInt, "", new Integer[0]),
                new Expectation<>(Integer[]::new, Integer::parseInt, "42", new Integer[] {42}),
                new Expectation<>(Integer[]::new, Integer::parseInt, "42 10 23", new Integer[] {42, 10, 23}),
                new Expectation<>(Double[]::new, Double::parseDouble, "", new Double[0]),
                new Expectation<>(Double[]::new, Double::parseDouble, "42.0", new Double[] {42.0}),
                new Expectation<>(Double[]::new, Double::parseDouble, "42.0 10.5 23.42",
                        new Double[] {42d, 10.5, 23.42}),
                new Expectation<>(Boolean[]::new, Boolean::parseBoolean, "", new Boolean[0]),
                new Expectation<>(Boolean[]::new, Boolean::parseBoolean, "true", new Boolean[] {true}),
                new Expectation<>(Boolean[]::new, Boolean::parseBoolean, "true false true",
                        new Boolean[] {true, false, true}),
                new Expectation<>(String[]::new, s -> s, "", new String[0]),
                new Expectation<>(String[]::new, s -> s, " ", new String[] {"", ""}),
                new Expectation<>(String[]::new, s -> s, "42", new String[] {"42"}),
                new Expectation<>(String[]::new, s -> s, "42 abc  DEF", new String[] {"42", "abc", "", "DEF"}),
        };
    }

    @Theory
    public void test(Expectation<?> expectation) {
        ArrayLineReader<?, List<Object[]>> reader = new ArrayLineReader<>(List::add, expectation.converter,
                expectation.arrayCreator);

        List<Object[]> parentMock = new ArrayList<>(1);
        Context context = new Context(Arrays.asList(expectation.line, "ignored line"));

        reader.readSection(parentMock, context, new Config());

        assertEquals(1, parentMock.size());
        assertArrayEquals(expectation.expectedOutput, parentMock.get(0));
        assertEquals("ignored line", context.readLine());
    }
}