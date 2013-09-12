package de.jowisoftware.mocking.results;

public class MockCallThrowableResult implements MockCallResult {
    private final Throwable result;

    public MockCallThrowableResult(final Throwable result) {
        this.result = result;
    }

    @Override
    public Object get() throws Throwable {
        throw result;
    }
}
