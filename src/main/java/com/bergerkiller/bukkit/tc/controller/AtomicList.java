package com.bergerkiller.bukkit.tc.controller;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;



public class AtomicList<T> implements List<T> {
    protected final AtomicReference<List<T>> items = new AtomicReference<>(new ArrayList<>());

    public List<T> getItems() {
        return items.get();
    }

    protected List<T> copyList() {
        return new ArrayList<>(items.get());
    }

    protected void updateItems(List<T> newList) {
        items.set(newList);
    }

    @Override
    public int size() {
        return items.get().size();
    }

    @Override
    public boolean isEmpty() {
        return items.get().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return items.get().contains(o);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return items.get().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return items.get().toArray(new Object[0]);
    }

    @Override
    public @NotNull <A> A @NotNull [] toArray(@NotNull A[] a) {
        return items.get().toArray(a);
    }

    @Override
    public boolean add(T minecartMember) {
        var newItems = copyList();
        newItems.add(minecartMember);
        updateItems(newItems);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        var newItems = copyList();
        boolean wasRemoved = newItems.remove(o);

        if(wasRemoved) {
            updateItems(newItems);
        }

        return wasRemoved;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(items.get()).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        var newItems = copyList();
        boolean wasAdded = newItems.addAll(c);

        if(wasAdded) {
            updateItems(newItems);
        }

        return wasAdded;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        var newItems = copyList();
        boolean wasAdded = newItems.addAll(index, c);

        if(wasAdded) {
            updateItems(newItems);
        }

        return wasAdded;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        var newItems = copyList();
        boolean wasRemoved = newItems.removeAll(c);

        if(wasRemoved) {
            updateItems(newItems);
        }

        return wasRemoved;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        var newItems = copyList();
        boolean wasRetained = newItems.retainAll(c);

        if(wasRetained) {
            updateItems(newItems);
        }

        return wasRetained;
    }

    @Override
    public void clear() {
        items.set(Collections.emptyList());
    }

    @Override
    public T get(int index) {
        return items.get().get(index);
    }

    @Override
    public T set(int index, T element) {
        var newItems = copyList();
        T oldElement = newItems.set(index, element);
        updateItems(newItems);
        return oldElement;
    }

    @Override
    public void add(int index, T element) {
        var newItems = copyList();
        newItems.add(index, element);
        updateItems(newItems);
    }

    @Override
    public T remove(int index) {
        var newItems = copyList();
        T oldElement = newItems.remove(index);
        updateItems(newItems);
        return oldElement;
    }

    @Override
    public int indexOf(Object o) {
        return items.get().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return items.get().lastIndexOf(o);
    }

    @Override
    public @NotNull ListIterator<T> listIterator() {
        return items.get().listIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return items.get().listIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        return items.get().subList(fromIndex, toIndex);
    }
}
