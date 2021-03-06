package org.hildan.hashcode.utils.parser.readers.container;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;

import org.hildan.hashcode.utils.parser.InputParsingException;
import org.hildan.hashcode.utils.parser.context.Context;
import org.hildan.hashcode.utils.parser.readers.ChildReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link ChildReader} that creates a container, and reads multiple child objects from the input, adding them to the
 * container.
 *
 * @param <E>
 *         the type of the elements in the container
 * @param <C>
 *         the type of the container itself
 * @param <P>
 *         the type of parent on which this {@code ContainerReader} sets the created container
 */
public class ContainerReader<E, C, P> implements ChildReader<C, P> {

    private final IntFunction<? extends C> constructor;

    private final ChildReader<? extends E, ? super P> itemReader;

    private final BiFunction<? super P, Context, Integer> getSize;

    private final AddFunction<? super E, ? super C> addFunction;

    /**
     * Creates a new {@code ContainerReader} that may read the expected number of items from the parent object or a
     * context variable using the provided size getter function.
     *
     * @param getSize
     *         a function to get the number of items to read, which is given the parent object and context as parameter.
     *         Note that the given parent parameter may be null if this reader is called to create a root object.
     * @param itemReader
     *         a child reader used to read each item
     * @param constructor
     *         a constructor to create a new container, given the size as input
     * @param addFunction
     *         a function to add elements to the created container
     */
    @SuppressWarnings("WeakerAccess")
    protected ContainerReader(BiFunction<? super P, Context, Integer> getSize,
            ChildReader<? extends E, ? super P> itemReader, IntFunction<? extends C> constructor,
            AddFunction<? super E, ? super C> addFunction) {
        this.constructor = constructor;
        this.itemReader = itemReader;
        this.getSize = getSize;
        this.addFunction = addFunction;
    }

    @Override
    public C read(@NotNull Context context, @Nullable P parent) throws InputParsingException {
        int size = getSize.apply(parent, context);
        C collection = constructor.apply(size);
        for (int i = 0; i < size; i++) {
            addFunction.apply(collection, i, itemReader.read(context, parent));
        }
        return collection;
    }

    /**
     * Creates a new {@code ContainerReader} that may read the expected number of items from the parent object or a
     * context variable.
     *
     * @param getSize
     *         a function to get the number of items to read, which is given the parent object and context as parameter.
     *         Note that the given parent parameter may be null if this reader is called to create a root object.
     * @param itemReader
     *         a child reader used to read each item
     * @param constructor
     *         a constructor to create a new container, given the size as input
     * @param addFunction
     *         a function to add elements to the created container
     * @param <E>
     *         the type of the elements in the container
     * @param <C>
     *         the type of the container itself
     * @param <P>
     *         the type of parent on which the created {@code ContainerReader} will set the created containers
     *
     * @return the created {@code ContainerReader}
     */
    public static <E, C, P> ChildReader<C, P> of(BiFunction<? super P, Context, Integer> getSize,
            ChildReader<? extends E, ? super P> itemReader, IntFunction<? extends C> constructor,
            AddFunction<? super E, ? super C> addFunction) {
        return new ContainerReader<>(getSize, itemReader, constructor, addFunction);
    }

    /**
     * Creates a {@code ContainerReader} that reads an array of items.
     *
     * @param getSize
     *         a function to get the number of items to read, which is given the parent object and context as parameter.
     *         Note that the given parent parameter may be null if this reader is called to create a root object.
     * @param itemReader
     *         a reader to call as many times as the size returned by {@code getSize}. This is what actually consumes
     *         input in the created reader.
     * @param arrayCreator
     *         a function to create a new array, given its size
     * @param <E>
     *         the type of elements in the array
     * @param <P>
     *         the type of parent that the created array will be part of
     *
     * @return the created {@code ContainerReader}
     */
    public static <E, P> ChildReader<E[], P> ofArray(BiFunction<? super P, Context, Integer> getSize,
            ChildReader<? extends E, ? super P> itemReader, IntFunction<E[]> arrayCreator) {
        return new ContainerReader<>(getSize, itemReader, arrayCreator, Array::set);
    }

    /**
     * Creates a {@code ContainerReader} that reads a list of items.
     *
     * @param getSize
     *         a function to get the number of items to read, which is given the parent object and context as parameter.
     *         Note that the given parent parameter may be null if this reader is called to create a root object.
     * @param itemReader
     *         a reader to call as many times as the size returned by {@code getSize}. This is what actually consumes
     *         input in the created reader.
     * @param <E>
     *         the type of elements in the list
     * @param <P>
     *         the type of parent that the created list will be part of
     *
     * @return the created {@code ContainerReader}
     */
    public static <E, P> ChildReader<List<E>, P> ofList(BiFunction<? super P, Context, Integer> getSize,
            ChildReader<? extends E, ? super P> itemReader) {
        return ofCollection(getSize, itemReader, ArrayList::new);
    }

    /**
     * Creates a {@code ContainerReader} that reads a collection of items.
     *
     * @param getSize
     *         a function to get the number of items to read, which is given the parent object and context as parameter.
     *         Note that the given parent parameter may be null if this reader is called to create a root object.
     * @param itemReader
     *         a reader to call as many times as the size returned by {@code getSize}. This is what actually consumes
     *         input in the created reader.
     * @param constructor
     *         a function to create a new collection, given its size
     * @param <E>
     *         the type of elements in the collection
     * @param <C>
     *         the type of collections to create
     * @param <P>
     *         the type of parent that the created collection will be part of
     *
     * @return the created {@code ContainerReader}
     */
    public static <E, C extends Collection<E>, P> ChildReader<C, P> ofCollection(
            BiFunction<? super P, Context, Integer> getSize, ChildReader<? extends E, ? super P> itemReader,
            IntFunction<C> constructor) {
        return new ContainerReader<>(getSize, itemReader, constructor, (c, i, e) -> c.add(e));
    }
}
