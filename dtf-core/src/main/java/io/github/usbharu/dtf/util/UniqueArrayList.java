package io.github.usbharu.dtf.util;

import java.util.ArrayList;
import java.util.Collection;

public class UniqueArrayList<T> extends ArrayList<T> {

  @Override
  public boolean add(T t) {
    if (contains(t)) {
      return false;
    }
    return super.add(t);
  }

  @Override
  public void add(int index, T element) {
    if (contains(element)) {
      return;
    }
    super.add(index, element);
  }

  /**
   * @param index   index of the element to replace
   * @param element element to be stored at the specified position
   * @return null or the element previously at the specified position
   */
  @Override
  public T set(int index, T element) {
    if (contains(element)) {
      return null;
    }
    return super.set(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    throw new UnsupportedOperationException();
  }
}
