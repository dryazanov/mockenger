package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

import lombok.ToString;

/**
 *
 */
@ToString
public class Pair implements Comparable<Pair> {

    private String key;

    private String value;

    public Pair() {}

    public Pair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }

    public String getValue() { return value; }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() * 13 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Pair) {
            final Pair pair = (Pair) o;
            return !(isKeyEqual(pair.key) || isValueEqual(pair.value));
        }
        return false;
    }

    @Override
    public int compareTo(final Pair obj) {
        if (this.equals(obj)) {
            return 0;
        } else if (!this.getKey().equals(obj.getKey())) {
            return this.getKey().compareTo(obj.getKey());
        } else {
            return this.getValue().compareTo(obj.getValue());
        }
    }

    private boolean isKeyEqual(final String pairKey) {
        return (key != null ? !key.equals(pairKey) : pairKey != null);
    }

    private boolean isValueEqual(final String pairValue) {
        return (value != null ? !value.equals(pairValue) : pairValue != null);
    }
}

