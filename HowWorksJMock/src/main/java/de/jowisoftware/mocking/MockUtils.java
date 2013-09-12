package de.jowisoftware.mocking;

import java.lang.reflect.Method;

public final class MockUtils {
    private MockUtils() {
    }

    public static String formatCall(final String name, final Method method,
            final Object[] arguments) {
        final StringBuilder builder = new StringBuilder();
        builder
                .append(name)
                .append(".")
                .append(method.getName())
                .append("(");

        for (int i = 0; i < arguments.length; ++i) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(arguments[i].toString());
        }

        builder.append(")");

        return builder.toString();
    }

    public static Object getDefaultValue(final Class<?> type) {
        if (type.equals(Integer.TYPE)) {
            return 0;
        } else if (type.equals(Boolean.TYPE)) {
            return false;
        } else if (type.equals(Long.TYPE)) {
            return 0L;
        } else if (type.equals(Short.TYPE)) {
            return (short) 0;
        } else if (type.equals(Byte.TYPE)) {
            return (byte) 0;
        } else if (type.equals(Float.TYPE)) {
            return 0f;
        } else if (type.equals(Double.TYPE)) {
            return 0.0;
        }
        return null;
    }
}
