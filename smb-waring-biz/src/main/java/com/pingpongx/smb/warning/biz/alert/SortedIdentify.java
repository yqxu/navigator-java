package com.pingpongx.smb.warning.biz.alert;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
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
}
