package de.jowisoftware.mocking;

import java.lang.reflect.Method;

public class MockInvocationHandler extends AbstractInvocationHandler {
    private static final Method toStringMethod;
    private static final Method hashCodeMethod;
    private static final Method equalsMethod;

    static {
        try {
            toStringMethod = Object.class.getMethod(
                    "toString", new Class[0]);
            hashCodeMethod = Object.class.getMethod(
                    "hashCode", new Class[0]);
            equalsMethod = Object.class.getMethod(
                    "equals", new Class[] { Object.class });
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public MockInvocationHandler(final String name,
            final ExpectationList expectationList) {
        super(name, expectationList);
    }

    @Override
    public Object invoke(final Method method,
            final Object[] arguments)
            throws Throwable {

        final MockCallResult result = handleMockCall(method, arguments);

        if (result.callWasExpected) {
            if (result.hasResult) {
                return result.result;
            } else {
                return getDefaultValue(method);
            }
        }

        final Object returnValue = handleDefaultMethods(method);
        if (returnValue != null) {
            return returnValue;
        } else {
            throw new AssertionError("Unexpected invocation: "
                    + formatCall(method, arguments));
        }
    }

    private MockCallResult handleMockCall(final Method method,
            final Object[] arguments) {
        return expectationList.registerCall(method, arguments);
    }

    private String formatCall(final Method method, final Object[] arguments) {
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

    private Object handleDefaultMethods(final Method method) {
        if (method.equals(toStringMethod)) {
            return name + " [mock]";
        } else if(method.equals(equalsMethod)) {
            return false;
        } else if (method.equals(hashCodeMethod)) {
            return this.hashCode();
        } else {
            return null;
        }
    }
}
