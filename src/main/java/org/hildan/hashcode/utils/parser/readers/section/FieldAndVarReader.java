package org.hildan.hashcode.utils.parser.readers.section;

import org.hildan.hashcode.utils.parser.InputParsingException;
import org.hildan.hashcode.utils.parser.context.Context;
import org.hildan.hashcode.utils.parser.reflect.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link SectionReader} that reads a value and injects it in a field of the parent object, and/or a context variable.
 *
 * @param <P>
 *         the type of parent that this {@code FieldAndVarReader} can update
 */
public class FieldAndVarReader<P> implements SectionReader<P> {

    private final String fieldName;

    private final String variableName;

    /**
     * Creates a new {@code FieldAndVarReader} with the given field/variable names.
     * <p>
     * Both the field and variable names are optional. If a field name is specified, the corresponding field is set to
     * the parsed value, converted to the proper type. If a variable name is specified, the corresponding variable is
     * set to the parsed value. If both are specified, then both are set to the same value, reading only one token from
     * the input.
     * <p>
     * Specifying neither a field nor a variable name is valid. In this case, this reader consumes one token from the
     * input nonetheless, but no fields nor variables are set as a result.
     *
     * @param fieldName
     *         the name of a field of the parent object, or null if no field should be set. The given field has to be of
     *         a primitive type or string.
     * @param variableName
     *         the name of a variable to set, or null if no variable should be set.
     */
    public FieldAndVarReader(String fieldName, String variableName) {
        this.fieldName = fieldName;
        this.variableName = variableName;
        if (fieldName != null && fieldName.isEmpty()) {
            throw new IllegalArgumentException("Empty field name is not allowed, should be null to omit field");
        }
        if (variableName != null && variableName.isEmpty()) {
            throw new IllegalArgumentException("Empty variable name is not allowed, should be null to omit variable");
        }
    }

    @Override
    public void readAndSet(@NotNull Context context, @Nullable P object) throws InputParsingException {
        String value = context.readString();
        if (variableName != null) {
            context.setVariable(variableName, value);
        }
        if (fieldName != null) {
            ReflectUtils.setField(object, fieldName, value);
        }
    }
}
