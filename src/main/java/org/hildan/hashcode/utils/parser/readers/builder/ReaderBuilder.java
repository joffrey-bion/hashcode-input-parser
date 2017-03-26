package org.hildan.hashcode.utils.parser.readers.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.hildan.hashcode.utils.parser.readers.RootReader;
import org.hildan.hashcode.utils.parser.readers.creators.Int3Creator;
import org.hildan.hashcode.utils.parser.readers.creators.Int4Creator;
import org.hildan.hashcode.utils.parser.readers.creators.Int5Creator;
import org.hildan.hashcode.utils.parser.readers.creators.ObjectCreator;

/**
 * A builder that allows to create a {@link RootReader} with pre-readers that initialize some state (such as context
 * variables) before calling the constructor. This is useful when using parameterized constructors.
 */
public class ReaderBuilder {

    private final List<StateReader> preReaders = new ArrayList<>();

    public ReaderBuilder add(StateReader reader) {
        preReaders.add(reader);
        return this;
    }

    /**
     * Creates a new {@code RootReader} that creates objects with the given constructor. The argument passed to the
     * constructor is taken from the given context variable, that must be set up front via {@link
     * RootReader#withVars(String...)}.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(Function<Integer, ? extends T> constructor, String varName) {
        return of(ctx -> constructor.apply(ctx.getVariableAsInt(varName)));
    }

    /**
     * Creates a new {@code RootReader} that creates objects with the given constructor. The arguments passed to the
     * constructor are taken from the given context variables, that must be set up front via {@link
     * RootReader#withVars(String...)}.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(BiFunction<Integer, Integer, ? extends T> constructor, String var1, String var2) {
        return of(ctx -> constructor.apply(ctx.getVariableAsInt(var1), ctx.getVariableAsInt(var2)));
    }

    /**
     * Creates a new {@code RootReader} that creates objects with the given constructor. The arguments passed to the
     * constructor are taken from the given context variables, that must be set up front via {@link
     * RootReader#withVars(String...)}.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(Int3Creator<? extends T> constructor, String var1, String var2, String var3) {
        return of(ctx -> constructor.create(ctx.getVariableAsInt(var1), ctx.getVariableAsInt(var2),
                ctx.getVariableAsInt(var3)));
    }

    /**
     * Creates a new {@code RootReader} that creates objects with the given constructor. The arguments passed to the
     * constructor are taken from the given context variables, that must be set up front via {@link
     * RootReader#withVars(String...)}.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(Int4Creator<T> constructor, String var1, String var2, String var3, String var4) {
        return of(ctx -> constructor.create(ctx.getVariableAsInt(var1), ctx.getVariableAsInt(var2),
                ctx.getVariableAsInt(var3), ctx.getVariableAsInt(var4)));
    }

    /**
     * Creates a new {@code RootReader} that creates objects with the given constructor. The arguments passed to the
     * constructor are taken from the given context variables, that must be set up front via {@link
     * RootReader#withVars(String...)}.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(Int5Creator<T> constructor, String var1, String var2, String var3, String var4,
                                String var5) {
        return of(ctx -> constructor.create(ctx.getVariableAsInt(var1), ctx.getVariableAsInt(var2),
                ctx.getVariableAsInt(var3), ctx.getVariableAsInt(var4), ctx.getVariableAsInt(var5)));
    }

    /**
     * Creates a new {@code RootReader} that executes this builder's pre-readers before calling the given constructor to
     * create the object.
     *
     * @param constructor
     *         the constructor to use to create new instances
     * @param <T>
     *         the type of objects that the new {@code RootReader} should create
     *
     * @return a new {@code RootReader}
     */
    public <T> RootReader<T> of(ObjectCreator<T> constructor) {
        return new RootReader<>(preReaders, constructor);
    }
}