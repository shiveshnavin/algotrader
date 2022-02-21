package com.semibit.stocksmate.automate.common.models;

import java.util.ArrayList;
import java.util.Collection;

public class SlidingWindow<E> extends ArrayList {

    public SlidingWindow(){
        super();
    }

    public SlidingWindow(Collection<? extends E> c){
        super(c);
        clear();
        addAll(c);
    }

    @Override
    public boolean add(Object o) {
        return super.add(o);
    }

    @Override
    public void add(int index, Object element) {
        super.add(index, element);
    }

    @Override
    public boolean addAll(Collection c) {
        return super.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return super.addAll(index, c);
    }
}
