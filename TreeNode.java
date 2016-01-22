/**
 * This code comes from https://github.com/vivin/GenericTree
 * It is used as the generic node for a subset tree
 */
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeNode<T> {
  
  public T data;
  public T indices;
  public int limit;
  public List<TreeNode<T>> children;
  
  public TreeNode() {
    super();
    children = new ArrayList<TreeNode<T>>();
  }
  
  public TreeNode(T data) {
    this();
    setData(data);
  }
  
  public List<TreeNode<T>> getChildren() {
    return this.children;
  }
  
  public int getNumberOfChildren() {
    return getChildren().size();
  }
  
  public boolean hasChildren() {
    return (getNumberOfChildren() > 0);
  }
  
  public void setChildren(List<TreeNode<T>> children) {
    /*for ( TreeNode<T> child : children ) {
      TreeNode<T> newChild = new TreeNode<T>( child.getData( ) );
      newChild.setIndices( child.getIndices( ) );
      newChild.setLimit( child.getLimit( ) );
      newChild.setChildren( child.getChildren( ) );
      children.add( newChild );
    }*/
    this.children = children;
  }
  
  public void addChild(TreeNode<T> child) {
    children.add(child);
  }
  
  public void addChildAt(int index, TreeNode<T> child) throws IndexOutOfBoundsException {
    children.add(index, child);
  }
  
  public void removeChildren() {
    this.children = new ArrayList<TreeNode<T>>();
  }
  
  public void removeChildAt(int index) throws IndexOutOfBoundsException {
    children.remove(index);
  }
  
  public TreeNode<T> getChildAt(int index) throws IndexOutOfBoundsException {
    return children.get(index);
  }
  
  public T getData() {
    return this.data;
  }
  
  public void setData(T data) {
    this.data = data;
  }
  
  public T getIndices() {
    return this.indices;
  }
  
  public void setIndices(T indices) {
    this.indices = indices;
  }
  
  public int getLimit() {
    return this.limit;
  }
  
  public void setLimit(int limit) {
    this.limit = limit;
  }
  
  public String toString() {
    return getData().toString();
  }
  
  public boolean equals(TreeNode<T> node) {
    return node.getData().equals(getData());
  }
  
  public int hashCode() {
    return getData().hashCode();
  }
  
  public String toStringVerbose() {
    String stringRepresentation = getData().toString() + ":[";
    
    for (TreeNode<T> node : getChildren()) {
      stringRepresentation += node.toStringVerbose() + ", ";
    }
    
    //Pattern.DOTALL causes ^ and $ to match
    Pattern pattern = Pattern.compile(", $", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(stringRepresentation);
    
    stringRepresentation = matcher.replaceFirst("");
    stringRepresentation += "]";
    
    return stringRepresentation;
  }
}

@SuppressWarnings("unchecked")
class NodeComparator implements Comparator {
  public int compare( Object o1, Object o2 ) {
    List<Integer> list1 = (List<Integer>)(((TreeNode)o1).getData());
    List<Integer> list2 = (List<Integer>)(((TreeNode)o2).getData());
    int sum1 = 0;
    int sum2 = 0;
    for ( int i = 0; i < list1.size(); ++i ) {
      sum1 += list1.get( i );
      sum2 += list2.get( i );
    }
    if ( sum1 > sum2 ) {
      return 1;
    }
    else if ( sum1 < sum2 ) {
      return -1;
    }
    return 0;  
  }
}