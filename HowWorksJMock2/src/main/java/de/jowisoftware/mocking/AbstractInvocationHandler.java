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
}
