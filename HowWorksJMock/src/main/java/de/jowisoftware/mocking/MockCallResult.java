package de.jowisoftware.mocking;

public class MockCallResult {
    public final boolean callWasExpected;
    public final boolean hasResult;
    public final Object result;

    public MockCallResult(final boolean callWasExpected,
            final boolean hasResult,
            final Object result) {
        this.callWasExpected = callWasExpected;
        this.hasResult = hasResult;
        this.result = result;
    }
}
