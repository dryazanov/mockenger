package com.socialstartup.mockenger.data.model.persistent.mock.request.part;

/**
 *
 */
public class Pair implements Comparable<Pair> {

    private String key;

    private String value;

    public Pair() {}

    public Pair(String key, String value) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
            if (value != null ? !value.equals(pair.value) : pair.value != null) return false;
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Pair obj) {
        if (this.equals(obj)) {
            return 0;
        } else if (!this.getKey().equals(obj.getKey())) {
            return this.getKey().compareTo(obj.getKey());
        } else {
            return this.getValue().compareTo(obj.getValue());
        }
    }
}

