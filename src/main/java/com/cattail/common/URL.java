package com.cattail.common;

import java.util.Objects;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class URL {
    private String ip;

    private Integer port;

    public URL(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        URL url = (URL) o;
        return Objects.equals(ip, url.ip) && Objects.equals(port, url.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
