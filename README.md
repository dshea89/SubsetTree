# Solving the Subset Sum Problem with Heap-Ordered Subset Trees

A heap-ordered tree structure consisting of n-length subsets of a set. To construct a subset tree (i.e. a collection of subset heaps) of order n from set S.

## Motivation

Solutions for the subset sum problem exist in which min-heap trees may be wielded to order all subsets by their sum and then traversed appropriately. By applying a binary search on the list of all sum-ordered subsets, a solution may be found relatively quickly. However, this approach only works for subsets in which all values are positive. The provided code extends this approach to include sets with all values, both positive and negative, by employing a new data structure termed the subset tree.

## Subset Tree Generation Algorithm

1. Sort the set.
2. Set the root node to the n smallest elements of S.
3. The first child is the subset of the parent node with the greatest element replaced by the next greatest element in S. If no such greater element exists, then you have reached the end of S and this child node should not be generated.
4. Let the variable i be equal to the second greatest index in the subset of the parent node. The next child is the subset
of the parent node with the element at index i replaced by the next greatest element in S. If that element already exists
in the subset, then increment the conflicting element with its next greatest element in S. Repeat with any additional conflicts until none exist or there is no greater element in S with which to increment. If the latter is the case, then you have reached the end of S and this child node should not be generated.
5. Decrement i by 1 and repeat Step 4 for all subsequent children until i is less than the smallest index at which the parent incremented its subset value.
6. The algorithm terminates when no additional incrementations can be made (i.e. when the greatest element in the subset is equal to the greatest element in S).

You will now have a min-heap of all subsets of length n from set S. For a subset heap of order n, the total number of nodes
is bounded at (N choose n), where N is the total number of elements in the input set. Each node has at most N children and the height of the tree is at most N.

## Subset Sum Solution Algorithm

Given an input set I of length N and a target sum S, the algorithm, from start to finish, may be defined as follows:

1. Sort I.
2. Offset each element in I by the absolute value of the least element in I plus one. Store this offset value.
3. Set a variable O to 1. This is the order of the virtual subset heap being searched.
4. While O is less than or equal to N and a subset that sums to S + (offset * O) has not been found:
  1. Perform a binary search for S + (offset ∗ O) on the virtual subset heap of order O.
  2. If a subset is found, then terminate the loop.
  3. If a subset is not found, then increment O by 1 and repeat Step 4a. Do this until a subset is found or O is greater than N.
5. If a subset has been found, then subtract the offset value from each element in the located subset. This scaled subset is the returned value. If no subset has been found, then indicate so to the user.

## Algorithmic Complexity

As with the heap-ordered binary tree, the binary search is done in time logarithmic to the size of the power set and each lookup is done in a time dependent on the k value. Since the number of child nodes at each deleted node in the subset tree can be N, the heapification step requires additional computation, with the overhead raising it from O(log k) to O(N log k); the lookup takes O(N k log k) time. Since this O(N^2 k log k) operation must be run N times in the worst case where no subset exists, its overall complexity is O(N^3 k log k).

## A Simple Example

Take the example set of {−7, −3, −2, 5, 8} with a target sum of 0. The algorithm is executed as follows:

1. {−7, −3, −2, 5, 8} is sorted. (In this case, it was already sorted.)
2. An offset of 8 is applied to {−7, −3, −2, 5, 8}, producing a scaled set of {1, 5, 6, 13, 16}.
3. O is set to 1.
4. A binary search for S + (offset * O), or 8, is performed on the virtual subset heap of order O, or 1.
5. Since no subset is found, O is incremented by 1. As O is not greater than the set size of 5, we may continue our search.
6. A binary search for S + (offset * O), or 16, is performed on the virtual subset heap of order O, or 2.
7. Since no subset is found, O is incremented by 1. As O is not greater than the set size of 5, we may continue our search.
8. A binary search for S + (offset * O), or 24, is performed on the virtual subset heap of order O, or 3.
9. The subset {5, 6, 13} is returned. Subtract the offset from each element to produce the original subset {−3, −2, 5}.
10. Return {−3, −2, 5}.

## Java Classes

The provided code will generate a subset heap of a specified order from hardcoded input.

* The TreeNode class has been taken from the Github repository [GenericTree](https://github.com/vivin/GenericTree) and maintains a generic node in the tree.
* The SubsetTree class is the executable class with the order and input set variables. This class specifies a hardcoded k value for finding the kth subset. This generates the whole tree.
* The VirtualSubsetTree class will generate all child nodes as needed. This generates the tree up to the kth subset and is done in-place, preventing generation of the complete subset tree.
* The SubsetSum class is the class which incorporates VirtualSubsetTree with the complete algorithm in order to solve the subset sum problem. An example set and target sum has been provided in the class and may be modified as desired.

## Further Information

A complete explanation is available on the arXiv at: http://arxiv.org/abs/1512.01727

This paper does a more detailed job of explaining the motivation for the algorithm, the theory behind it, and its complete implementation.