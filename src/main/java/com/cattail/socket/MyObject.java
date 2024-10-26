package com.cattail.socket;

import java.io.Serializable;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/26
 * @Copyright: https://github.com/CatTailzz
 */
public class MyObject implements Serializable {
    private static final long serialVersionID = 1L;
    private String filed1;
    private String filed2;

    public MyObject(String filed1, String filed2) {
        this.filed1 = filed1;
        this.filed2 = filed2;
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "filed1='" + filed1 + '\'' +
                ", filed2='" + filed2 + '\'' +
                '}';
    }
}
