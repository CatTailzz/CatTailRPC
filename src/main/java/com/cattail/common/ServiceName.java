package com.cattail.common;

import java.util.Objects;

/**
 * @description:
 * @author：CatTail
 * @date: 2024/10/28
 * @Copyright: https://github.com/CatTailzz
 */
public class ServiceName {

    private final String name;

    private final String version;

    public ServiceName(String name, String version) {

        this.name = name;
        this.version = version;
    }

    @Override
    public String toString() {
        return "ServiceName{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceName that = (ServiceName) o;
        return Objects.equals(name, that.name) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
