package com.simple.arouter;

import java.io.Serializable;

public class TestSerializable implements Serializable {
    String name = "TestSerializable";

    @Override
    public String toString() {
        return "TestSerializable{" +
            "name='" + name + '\'' +
            '}';
    }
}
