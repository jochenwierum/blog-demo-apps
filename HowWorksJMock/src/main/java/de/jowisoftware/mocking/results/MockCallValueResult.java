package de.jowisoftware.mocking.results;

public class MockCallValueResult implements MockCallResult {
    private final Object result;

    public MockCallValueResult(final Object result) {
        this.result = result;
    }

    @Override
    public Object get() {
        return result;
    }
}
