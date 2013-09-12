package de.jowisoftware.mocking;

public class MockObjectContext<T> {
    private final T programmable;
    private final String name;
    private final ExpectationList expectationList;

    public MockObjectContext(final String name,
            final T programmable,
            final ExpectationList expecationList) {
        this.name = name;
        this.programmable = programmable;
        this.expectationList = expecationList;
    }

    public String getName() {
        return name;
    }

    public ExpectationList getExpectationList() {
        return expectationList;
    }

    public T getProgrammable() {
        return programmable;
    }
}
