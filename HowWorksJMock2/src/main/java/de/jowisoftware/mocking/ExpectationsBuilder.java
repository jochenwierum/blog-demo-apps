package de.jowisoftware.mocking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;

public abstract class ExpectationsBuilder {
    private Map<Object, MockObjectContext<?>> mocks;
    private MockObjectContext<?> lastMock;
    private final List<Matcher<?>> matchers = new ArrayList<>();

    protected final void build(final Map<Object, MockObjectContext<?>> mocks) {
        this.mocks = mocks;
        build();
        sealLastCall();
    }

    abstract void build();

    protected <T> T oneOf(final T mockObject) {
        sealLastCall();

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

    protected <T> T with(final Matcher<T> matcher) {
        appendParameterMatcher(matcher);
        return null;
    }

    protected int withInt(final Matcher<Integer> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public byte withByte(final Matcher<Byte> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public float withFloat(final Matcher<Float> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public double withDouble(final Matcher<Double> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public long withLong(final Matcher<Long> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public short withShort(final Matcher<Short> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    public char withChar(final Matcher<Character> matcher) {
        appendParameterMatcher(matcher);
        return 0;
    }

    private void appendParameterMatcher(final Matcher<?> matcher) {
        matchers.add(matcher);
    }

    private void sealLastCall() {
        if (lastMock != null && matchers.size() > 0) {
            final Matcher<?>[] matchersArray = matchers
                    .toArray(new Matcher<?>[matchers.size()]);
            lastMock.getExpectationList().registerMatcherToLastCall(
                    matchersArray);
        }
        matchers.clear();
    }

}
