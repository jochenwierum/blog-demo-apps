package de.jowisoftware.mocking;

import java.lang.reflect.Method;

public class ProgrammableInvocationHandler extends AbstractInvocationHandler {

    public ProgrammableInvocationHandler(final String name,
            final ExpectationList expectationList) {
        super(name, expectationList);
    }

    @Override
    protected Object invoke(final Method method, final Object[] arguments) {
        expectationList.expectCall(method, arguments);
        return getDefaultValue(method);
    }

}
