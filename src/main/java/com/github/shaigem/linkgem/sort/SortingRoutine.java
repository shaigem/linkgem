package com.github.shaigem.linkgem.sort;

import com.github.shaigem.linkgem.model.item.Item;

/**
 * The base class for all sorting algorithms.
 *
 * @author Ronnie Tran
 */
public abstract class SortingRoutine {

    /**
     * The name of the sorting routine.
     *
     * @return the name
     */
    public abstract String name();

    /**
     * Sorts the given array by ascending order.
     *
     * @param array the array to sort
     * @return the sorted array
     */
    public abstract Item[] sortAscending(Item[] array);

    /**
     * Sorts the given array by descending order.
     *
     * @param array the array to sort
     * @return the sorted array
     */
    public abstract Item[] sortDescending(Item[] array);

    /**
     * Sorts the given array by a given order.
     *
     * @param sortOrder the sort order. It can be ascending or descending
     * @param array     the array to sort
     * @return the sorted array
     */
    public Item[] sort(final SortOrder sortOrder, Item[] array) {
        // create a copy of the given array
        // we don't want to directly sort items in the given array but
        // we will instead have the sorting algorithm return a new array that contains the sorted data
        Item[] copyOfArray = new Item[array.length];
        System.arraycopy(array, 0, copyOfArray, 0, array.length);
        // measure the time it takes to execute the sorting
        return sortOrder == SortOrder.ASCENDING ? sortAscending(copyOfArray) : sortDescending(copyOfArray);
    }

    /**
     * Swaps the values of two elements in the given array.
     *
     * @param array the array to swap values
     * @param i     the first element
     * @param j     the second element
     */
    protected void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
