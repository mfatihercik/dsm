package com.github.mfatihercik.dsb.model;

public class Default {
    String value;
    boolean force;
    boolean atStart;  //TODO test at start

    public Default(String value) {
        this.value = value;
    }

    public Default(String value, boolean force, boolean atStart) {
        this.value = value;
        this.force = force;
        this.atStart = atStart;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isAtStart() {
        return atStart;
    }

    public void setAtStart(boolean atStart) {
        this.atStart = atStart;
    }
}
