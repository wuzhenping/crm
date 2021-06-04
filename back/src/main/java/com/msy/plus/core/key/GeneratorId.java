package com.msy.plus.core.key;

public class GeneratorId {
    Sequence iw = null;
    //构造方法
    private GeneratorId() {
        iw = new Sequence(System.currentTimeMillis());

    }

    private static GeneratorId instance = null;

    public static synchronized GeneratorId getInstance() {
        if (instance == null) {
            instance = new GeneratorId();
        }
        return instance;
    }

    public long nextId() {
        return iw.getId();
    }

    public String nextCode(int len) {
        return iw.getCode(len);
    }
}
