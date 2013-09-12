package de.jowisoftware.mocking;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MockContext {
    private final Map<Object, MockObjectContext<?>> mocks = new HashMap<>();

    public <T> T mock(final Class<T> interfaceClass) {
        return mock(interfaceClass, interfaceClass.getClass().getSimpleName());
    }

    public <T> T mock(final Class<T> interfaceClass, final String name) {
        final ExpectationList expectationList = new ExpectationList();
        final T mock = createMock(name, interfaceClass, expectationList);
        final T programmable = createProgrammable(name, interfaceClass,
                expectationList);

        mocks.put(mock, new MockObjectContext<T>(name, programmable,
                expectationList));

        return mock;
    }

    @SuppressWarnings("unchecked")
    private <T> T createMock(final String name, final Class<T> interfaceClass,
            final ExpectationList expectationList) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { interfaceClass }, new MockInvocationHandler(
                        name,
                        expectationList));
    }

    @SuppressWarnings("unchecked")
    private <T> T createProgrammable(final String name,
            final Class<T> interfaceClass,
            final ExpectationList expectationList) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { interfaceClass },
                new ProgrammableInvocationHandler(name,
                        expectationList));
    }

    public void defineExpectations(final ExpectationsBuilder expectationsBuilder) {
        expectationsBuilder.build(mocks);
    }

    public void verify() {
        for (final MockObjectContext<?> entry : mocks.values()) {
            if (!entry.getExpectationList().isEmpty()) {
                throw new AssertionError(
                        "Not all expected methods were called on "
                                + entry.getName());
            }
        }
    }
}
