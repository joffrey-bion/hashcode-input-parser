package org.hildan.hashcode.utils.parser.config;

import org.intellij.lang.annotations.RegExp;

/**
 * Encapsulates the configuration that defines the readers' behaviour.
 */
public class Config {

    @RegExp
    private static final String DEFAULT_SEPARATOR = "\\s";

    @RegExp
    private final String separator;

    /**
     * Creates a new config with the default separator {@value #DEFAULT_SEPARATOR}.
     */
    public Config() {
        this.separator = DEFAULT_SEPARATOR;
    }

    /**
     * Creates a new config with the given separator.
     *
     * @param separator
     *         the separator between elements in an input line
     */
    public Config(@RegExp String separator) {
        this.separator = separator;
    }

    /**
     * Gets the separator between values within a single line of the input. The value is a regular expression, so that
     * it can directly be used in {@link String#split(String)} for instance.
     *
     * @return the separator as a regular expression
     */
    @RegExp
    public String getSeparator() {
        return separator;
    }
}
