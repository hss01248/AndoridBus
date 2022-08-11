package com.hss01248.bus;

import java.util.ArrayList;
import java.util.List;

/**
 * @Despciption todo
 * @Author hss
 * @Date 11/08/2022 15:46
 * @Version 1.0
 */
public class GenericTree {

    public int index;
    public Class clazz;
    public List<GenericTree> classes = new ArrayList<>();

    public GenericTree(Class clazz) {
        this.clazz = clazz;
    }
}
