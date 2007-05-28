package org.sodeja.collections;

import java.util.ArrayList;
import java.util.Collection;

import org.sodeja.functional.Predicate1;

// TODO fix this
public class UniqueList<E> extends ArrayList<E> {

    private static final long serialVersionUID = -8699432868653288992L;

    @Override
    public boolean add(E e) {
        if(! contains(e)) {
            return super.add(e);
        }
        return false;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not implemented"); //$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean addAll(Collection<? extends E> c) {
        return super.addAll(CollectionUtils.filter((Collection<E>) c, new ArrayList<E>(), new Predicate1<E>() {
            public Boolean execute(E p) {
                return contains(p);
            }}));
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not implemented"); //$NON-NLS-1$
    }
}
