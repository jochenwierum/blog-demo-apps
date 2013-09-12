package de.jowisoftware.proxydemo.proxy;

public class ClientClass implements ClientInterface1, ClientInterface2 {
    @Override
    public void runWithException() {
        throw new TestException();
    }

    @Override
    public int runWithoutException(final int result) {
        return result;
    }
}
