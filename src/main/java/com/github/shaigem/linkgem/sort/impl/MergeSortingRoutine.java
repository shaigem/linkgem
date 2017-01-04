package com.github.shaigem.linkgem.sort.impl;


import com.github.shaigem.linkgem.model.item.Item;
import com.github.shaigem.linkgem.sort.SortingRoutine;

/**
 * The merge sorting algorithm for sorting bookmark items by their names.
 *
 * @author Ronnie Tran
 */
public class MergeSortingRoutine extends SortingRoutine {

    @Override
    public String name() {
        return "Merge Sort";
    }

    @Override
    public Item[] sortAscending(Item[] array) {
        return mergeSort(array, 0, array.length - 1, false);
    }

    @Override
    public Item[] sortDescending(Item[] array) {
        return mergeSort(array, 0, array.length - 1, true);
    }

    /**
     * Sorts an array using the Merge Sorting algorithm. This splits the lists apart.
     *
     * @param array      the array to perform sorting on
     * @param left       the left-most index in the array
     * @param right      the right-most index in the array
     * @param descending whether or not the algorithm should sort it by descending order
     * @return the sorted array
     */
    private Item[] mergeSort(Item[] array, int left, int right, boolean descending) {
        if (left == right) {
            return new Item[]{array[left]};
        } else {
            int middle = (left + right) / 2;
            // split left
            Item[] sortedLeftHalfArray = mergeSort(array, left, middle, descending);
            // split right
            Item[] sortedRightHalfArray = mergeSort(array, middle + 1, right, descending);
            // merge splitted lists
            return merge(sortedLeftHalfArray, sortedRightHalfArray, descending);
        }
    }

    /**
     * Merges the sorted arrays together.
     *
     * @param sortedLeftHalfArray  the left part of the sorted array
     * @param sortedRightHalfArray the right part of the sorted array
     * @param descending           whether or not the algorithm should sort the elements by descending order
     * @return the final merged sorted array
     */
    private Item[] merge(Item[] sortedLeftHalfArray, Item[] sortedRightHalfArray, boolean descending) {
        Item[] resultArray = new Item[sortedLeftHalfArray.length + sortedRightHalfArray.length];
        int i = 0; // left index
        int j = 0; // right index
        int k = 0; // result index
        while (i < sortedLeftHalfArray.length && j < sortedRightHalfArray.length) { // contains unused numbers still
            String leftHalfName = sortedLeftHalfArray[i].getName().toLowerCase();
            String rightHalfName = sortedRightHalfArray[j].getName().toLowerCase();
            if (descending ? (leftHalfName.compareTo(rightHalfName) >=
                   rightHalfName.compareTo(leftHalfName)) :
                    (leftHalfName.compareTo(rightHalfName) <= rightHalfName.compareTo(leftHalfName))) {
                resultArray[k] = sortedLeftHalfArray[i];
                i++;
            } else {
                resultArray[k] = sortedRightHalfArray[j];
                j++;
            }
            k++;
        }
        if (i == sortedLeftHalfArray.length) {
            for (int m = j; m < sortedRightHalfArray.length; m++) {
                resultArray[k] = sortedRightHalfArray[m];
                k++;
            }
        } else if (j == sortedRightHalfArray.length) {
            for (int m = i; m < sortedLeftHalfArray.length; m++) {
                resultArray[k] = sortedLeftHalfArray[m];
                k++;
            }
        }
        return resultArray;
    }
}
