package com.pingpongx.smb.export.module;

public class SortedIdentify<I> implements Comparable<SortedIdentify<I>> {
    int sortBy;
    I identify;

    @Override
    public int compareTo(SortedIdentify o) {
        return  this.sortBy - o.sortBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SortedIdentify<?> that = (SortedIdentify<?>) o;

        return identify.equals(that.identify);
    }

    @Override
    public int hashCode() {
        return identify.hashCode();
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public I getIdentify() {
        return identify;
    }

    public void setIdentify(I identify) {
        this.identify = identify;
    }
}
