package de.jowisoftware.myDI.integrationtests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.jowisoftware.myDI.DIContext;
import de.jowisoftware.myDI.DIXmlContext;
import de.jowisoftware.myDI.integrationtests.test3.Class1;

/*
 * Third goal: after wiring is done, beans with special interfaces must be
 * initialized. There should be a special order, as this test shows.
 */

public class Test3IT {
    @Test
    public void contextIsInitializedCorrectly() {
        final DIContext context = new DIXmlContext(
                "/integrationTests/dependencies.test3.xml");

        final Class1 bean = (Class1) context.getBean("bean");

        assertThat(bean.getZ(), is(7));
        assertThat(bean.getName2(), is(equalTo("bean")));
    }
}
