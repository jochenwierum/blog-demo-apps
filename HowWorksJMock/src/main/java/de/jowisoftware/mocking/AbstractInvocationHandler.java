package de.jowisoftware.mocking;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler implements InvocationHandler {
    private static final Object[] EMPTY_ARGUMENTS = new Object[0];
    protected final ExpectationList expectationList;
    protected final String name;

    public AbstractInvocationHandler(final String name,
            final ExpectationList expectationList) {
        this.name = name;
        this.expectationList = expectationList;
    }

    @Override
    public final Object invoke(final Object proxy, final Method method,
            final Object[] argumentsOrNull)
            throws Throwable {
        final Object[] arguments;
        if (argumentsOrNull == null) {
            arguments = EMPTY_ARGUMENTS;
        } else {
            arguments = argumentsOrNull;
        }

        return invoke(method, arguments);
    }

    protected abstract Object invoke(Method method, Object[] arguments)
            throws Throwable;

    protected Object getDefaultValue(final Method method) {
        final Class<?> returnType = method.getReturnType();
        if (returnType.equals(Integer.TYPE)) {
            return 0;
        } else if (returnType.equals(Boolean.TYPE)) {
            return false;
        } else if (returnType.equals(Long.TYPE)) {
            return 0L;
        } else if (returnType.equals(Short.TYPE)) {
            return (short) 0;
        } else if (returnType.equals(Byte.TYPE)) {
            return (byte) 0;
        } else if (returnType.equals(Float.TYPE)) {
            return 0f;
        } else if (returnType.equals(Double.TYPE)) {
            return 0.0;
        }
        return null;
    }
}
