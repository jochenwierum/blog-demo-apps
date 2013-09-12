package de.jowisoftware.proxydemo.proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.jowisoftware.proxydemo.database.Database;

public class ProxyTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private Database database;
    private TransactionProxy proxy;

    private void expectDatabaseCalls(final boolean expectCommit) {
        final Sequence sequence = context.sequence("dbsequence");

        context.checking(new Expectations() {{
                oneOf(database).beginTransaction();
                inSequence(sequence);

                if (expectCommit) {
                    oneOf(database).commitTransaction();
                } else {
                    oneOf(database).rollbackTransaction();
                }
                inSequence(sequence);
        }});
    }

    @Before
    public void setUp() {
        database = context.mock(Database.class);
        proxy = new TransactionProxy(database);
    }

    @Test
    public void successfulOperationsAreCommited() {
        expectDatabaseCalls(true);
        final ClientInterface1 interface1 = proxy
                .createProxy(new ClientClass());

        final int expected = 10;
        final int actual = interface1.runWithoutException(expected);

        assertThat(actual, is(expected));
    }

    @Test(expected = TestException.class)
    public void unsuccessfulOperationsAreRolledBack() {
        expectDatabaseCalls(false);
        final ClientInterface2 interface1 = proxy
                .createProxy(new ClientClass());

        interface1.runWithException();
    }

    @Test(expected = ClassCastException.class)
    public void proxyCanNotBeCasted() {
        @SuppressWarnings("unused")
        final ClientClass error = proxy
                .createProxy(new ClientClass());
    }
}
