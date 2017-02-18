/*
 * This file is part of Fallout.
 *
 * Copyright (c) 2013-2017 <http://github.com/ampayne2/Fallout//>
 *
 * Fallout is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Fallout is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Fallout.  If not, see <http://www.gnu.org/licenses/>.
 */
package ninja.amp.fallout.util;

/**
 * Evaluates expressions used for various purposes in fallout.
 *
 * @author Austin Payne
 */
public class Expression {

    private Expression() {
    }

    /**
     * Evaluates an expression adhering to the following set of rules<br>
     * <pre>{@code
     *     expr   = term{+term|-term}
     *     term   = factor{*factor|/factor}
     *     factor = (expr)|number
     * }</pre>
     *
     * @param expression The expression to be evaluated
     * @return The expression's value
     */
    public static int evaluate(String expression) {
        StringBuilder source = new StringBuilder(expression);
        source.append(';');

        return expressionValue(source);
    }

    private static int expressionValue(StringBuilder source) {
        int value = termValue(source);

        char op = source.charAt(0);
        while (op == '+' || op == '-') {
            source.deleteCharAt(0);
            if (op == '+') {
                value += termValue(source);
            } else {
                value -= termValue(source);
            }
            op = source.charAt(0);
        }

        return value;
    }

    private static int termValue(StringBuilder source) {
        int value = factorValue(source);

        char op = source.charAt(0);
        while (op == '*' || op == '/') {
            source.deleteCharAt(0);
            if (op == '*') {
                value *= factorValue(source);
            } else {
                value /= factorValue(source);
            }
            op = source.charAt(0);
        }

        return value;
    }

    private static int factorValue(StringBuilder source) {
        int value = 0;

        char c = source.charAt(0);
        if (c == '(') {
            source.deleteCharAt(0);
            value = expressionValue(source);
            source.deleteCharAt(0);
        } else {
            while (Character.isDigit(c)) {
                source.deleteCharAt(0);
                value *= 10;
                value += Character.digit(c, 10);
                c = source.charAt(0);
            }
        }

        return value;
    }

}
