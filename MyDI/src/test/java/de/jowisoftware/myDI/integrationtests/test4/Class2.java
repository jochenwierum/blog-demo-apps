package de.jowisoftware.myDI.integrationtests.test4;

import de.jowisoftware.myDI.functions.Initializable;

public class Class2 implements Initializable {
    @Override
    public void initialize() {
        throw new IllegalStateException("test");
    }
}
