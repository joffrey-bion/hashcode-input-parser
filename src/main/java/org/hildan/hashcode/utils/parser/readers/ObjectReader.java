package org.hildan.hashcode.utils.parser.readers;

import org.hildan.hashcode.utils.parser.InputParsingException;
import org.hildan.hashcode.utils.parser.context.Context;
import org.jetbrains.annotations.NotNull;

/**
 * Reads an object from the current {@link Context}, consuming as many lines as necessary.
 *
 * @param <T>
 *         the type of object this {@code ObjectReader} creates
 */
public interface ObjectReader<T> {

    /**
     * Reads an object from the given {@link Context}, consuming as many lines as necessary.
     *
     * @param context
     *         the context to read lines from
     * @return the created object, may be null
     *
     * @throws InputParsingException
     *         if something went wrong when reading the input
     */
    T read(@NotNull Context context) throws InputParsingException;
}
