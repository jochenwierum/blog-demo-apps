package de.jowisoftware.mocking;

import java.util.Map;

public abstract class ExpectationsBuilder {
    private Map<Object, MockObjectContext<?>> mocks;
    private MockObjectContext<?> lastMock;

    protected final void build(final Map<Object, MockObjectContext<?>> mocks) {
        this.mocks = mocks;
        build();
    }

    abstract void build();

    protected <T> T oneOf(final T mockObject) {
        @SuppressWarnings("unchecked")
        final MockObjectContext<T> mock = (MockObjectContext<T>) mocks
                .get(mockObject);

        if (mock == null) {
            throw new IllegalArgumentException(mockObject + " is not a mock!");
        }

        lastMock = mock;
        return mock.getProgrammable();
    }

    protected void willReturn(final Object result) {
        lastMock.getExpectationList().addResultToLastCall(result);
    }

    protected void willThrow(final Throwable throwable) {
        lastMock.getExpectationList().addThrowableToLastCall(throwable);
    }
}
