/**
 * Building a Subset Tree (a collection of Subset Heaps) of size n from set S:
 *   1. Sort the set
 *   2. Set the root node to the n smallest elements of S
 *   3. The first child is the subset of the parent node with the greatest element replaced by
 *      the next greatest element in S. If no such greater element exists, then you have reached
 *      the end of S and this child node should not be generated.
 *   4. Let the variable i be equal to the second greatest index in the subset of the parent
 *      node. The next child is the subset of the parent node with the element at index i
 *      replaced by the next greatest element in S. If that element already exists in the subset,
 *      then increment the conflicting element with its next greatest element in S. Repeat with
 *      any additional conflicts until none exist or there is no greater element in S with which
 *      to increment. If the latter is the case, then you have reached the end of S and this
 *      child node should not be generated.
 *   5. Decerement i by 1 and repeat Step 4 for all subsequent children until i is less than the
 *      smallest index at which the parent incremented its subset value.
 *   6. The algorithm terminates when no additional incrementations can be made (i.e. when the
 *      greatest element in the subset is equal to the greatest element in S).
 * You will now have a min-heap of all subsets of length n from set S.
 * 
 * @author Dan Shea
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SubsetTree {
  
  public int subsetLen = 4;
  public ArrayList<Integer> input = new ArrayList<Integer>();
  public TreeNode<List<Integer>> tree;
  
  public SubsetTree() {
    input.add( 1 );
    input.add( 2 );
    input.add( 3 );
    input.add( 4 );
    input.add( 5 );
    input.add( 6 );
    
    // 1. Sort the set
    Collections.sort( input );
    
    tree = buildTree( input );
    
    // You will now have a min-heap of all subsets of length n from set S
    System.out.println( tree.toStringVerbose( ) );
    
    int k = 4;
    List<Integer> result = findKthMin( tree, k );
    printList( result );
  }
  
  public TreeNode<List<Integer>> buildTree( ArrayList<Integer> list ) {
    TreeNode<List<Integer>> tree = new TreeNode<List<Integer>>();
    
    ArrayList<Integer> idcs = new ArrayList<Integer>();
    for ( int i = 0; i < subsetLen; ++i ) {
      idcs.add( i );
    }
    
    // 2. Set the root node to the n smallest elements of S
    tree.setData( list.subList( 0, subsetLen ) );
    tree.setIndices( idcs );
    tree.setLimit( 0 );
    tree.setChildren( buildChildren( list.subList( 0, subsetLen ), idcs, 0 ) );
    
    return tree;
  }
  
  public List<TreeNode<List<Integer>>> buildChildren( List<Integer> list, List<Integer> indices, int limit ) {
    ArrayList<TreeNode<List<Integer>>> children = new ArrayList<TreeNode<List<Integer>>>();
    
    // 3. The first child is the subset of the parent node with the greatest element replaced by
    //    the next greatest element in S. If no such greater element exists, then you have reached
    //    the end of S and this child node should not be generated.
    // 4. Let the variable i be equal to the second greatest index in the subset of the parent
    //    node. The next child is the subset of the parent node with the element at index i
    //    replaced by the next greatest element in S. If that element already exists in the subset,
    //    then increment the conflicting element with its next greatest element in S. Repeat with
    //    any additional conflicts until none exist or there is no greater element in S with which
    //    to increment. If the latter is the case, then you have reached the end of S and this
    //    child node should not be generated.
    // 5. Decerement i by 1 and repeat Step 4 for all subsequent children until i is less than the
    //    smallest index at which the parent incremented its subset value.
    for ( int i = list.size( ) - 1; i >= limit; --i ) {
      // 6. The algorithm terminates when no additional incrementations can be made (i.e. when the
      //    greatest element in the subset is equal to the greatest element in S).
      if ( indices.get( indices.size( ) - 1 ) == input.size( ) - 1 ) {
        continue;
      }
      TreeNode<List<Integer>> child = new TreeNode<List<Integer>>();
      ArrayList<ArrayList<Integer>> tmpArr = incrementList( list, indices, i );
      child.setData( tmpArr.get( 0 ) );
      child.setIndices( tmpArr.get( 1 ) );
      child.setLimit( i );
      child.setChildren( buildChildren( child.getData( ), child.getIndices( ), child.getLimit( ) ) );
      children.add( child );
    }
    
    return children;
  }
  
  public ArrayList<ArrayList<Integer>> incrementList( List<Integer> list, List<Integer> indices, int idx ) {
    if ( list.size() <= 1 ) {
      return null;
    }
    ArrayList<Integer> newList = deepCopy( list );
    ArrayList<Integer> newIndices = deepCopy( indices );
    do {
      newList.set( idx, input.get( indices.get( idx ) + 1 ) );
      newIndices.set( idx, indices.get( idx ) + 1 );
      ++idx;
    } while ( idx < list.size( ) && newIndices.get( idx - 1 ) == indices.get( idx ) );
    if ( newIndices.get( newIndices.size( ) - 1 ) == newIndices.get( newIndices.size( ) - 2 ) ) {
      return null;
    }
    ArrayList<ArrayList<Integer>> retVal = new ArrayList<ArrayList<Integer>>();
    retVal.add( newList );
    retVal.add( newIndices );
    return retVal;
  }
  
  public ArrayList<Integer> deepCopy( List<Integer> list ) {
    ArrayList<Integer> newList = new ArrayList<Integer>();
    for ( int i = 0; i < list.size(); ++i ) {
      newList.add( list.get( i ) );
    }
    return newList;
  }
  
  @SuppressWarnings("unchecked")
  public List<Integer> findKthMin( TreeNode<List<Integer>> tree, int k ) {
    Comparator<TreeNode<List<Integer>>> comparator = new NodeComparator();
    PriorityQueue<TreeNode<List<Integer>>> toVisit = new PriorityQueue<TreeNode<List<Integer>>>( 11, comparator );
    TreeNode<List<Integer>> root = new TreeNode<List<Integer>>( tree.getData( ) );
    root.setIndices( tree.getIndices( ) );
    root.setLimit( tree.getLimit( ) );
    root.setChildren( tree.getChildren( ) );
    toVisit.add( root );
    ArrayList<TreeNode<List<Integer>>> smallestNodes = new ArrayList<TreeNode<List<Integer>>>( );
    while ( smallestNodes.size( ) < k ) {
      TreeNode<List<Integer>> node = toVisit.poll( );
      for ( TreeNode<List<Integer>> child : node.getChildren( ) ) {
        TreeNode<List<Integer>> newChild = new TreeNode<List<Integer>>( child.getData( ) );
        newChild.setIndices( child.getIndices( ) );
        newChild.setLimit( child.getLimit( ) );
        newChild.setChildren( child.getChildren( ) );
        toVisit.add( newChild );
      }
      smallestNodes.add( node );
    }
    return smallestNodes.get( k - 1 ).getData( );
  }
  
  public int sum( List<Integer> node ) {
    int sum = 0;
    for ( Integer n : node ) {
      sum += n;
    }
    return sum;
  }
  
  public void printList( List<Integer> list ) {
    for ( int i = 0; i < list.size( ); ++i ) {
      System.out.print( list.get( i ) + "\t" );
    }
    System.out.println( );
  }
  
  public static void main(String[] args) {
    new SubsetTree();
  }
  
}
