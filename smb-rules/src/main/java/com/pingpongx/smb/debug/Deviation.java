package com.pingpongx.smb.debug;

public class Deviation<E,A> {
    E expected;
    A actual;

    public E getExpected() {
        return expected;
    }

    public void setExpected(E expected) {
        this.expected = expected;
    }

    public A getActual() {
        return actual;
    }

    public void setActual(A actual) {
        this.actual = actual;
    }
}
