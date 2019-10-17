// Copyright (c) 2019 Daniel Lovasko
// All Rights Reserved
//
// Distributed under the terms of the 2-clause BSD License. The full
// license is in the file LICENSE, distributed as part of this software.

package sk.dlo.conifer;

import java.util.Optional;
import java.util.stream.Stream;


/**
 * The conifer library implements an AVL tree data structure.
 */
public final class Tree<Key extends Comparable<Key>, Value> {

  private final class Node {
    public Node left;
    public Node right;
    public Key key;
    public Value value;
    public long balance;

    public Node(final Key key, final Value value) {
      this.key     = key;
      this.value   = value;
      this.left    = null;
      this.right   = null;
      this.balance = 0;
    }
  }

  private long count;
  private Node root;
  private Stack<Node> path;

  /**
   * @brief Initialise an empty tree.
   */
  public Tree() {
    length = 0;
    root = null;

    // Create a path of nodes that can be used to track algorithm steps for further usage. Given
    // the performance-oriented goals of the implementation, it would be unwise to perform
    // allocation in each of the methods respectively, as that would slow down the critical path.
    // Therefore, the allocation happens only once - in the constructor method. This method also
    // ensures the capacity of the stack. The practical number of nodes in the tree is limited by
    // many aspects, mostly the current computer architectures.
    path = new Stack<>();
    path.ensureCapacity(128);
  }
  
  /**
   * @brief Add a value to the tree, possibly overwriting an existing one.
   * @param key key to insert
   * @param value value to insert
   *
   * @return old value associated with the key (if any)
   */
  public Optional<Value> insert(final Key key, final Value value) {
    // Early exit if this is the first node.
    if (root == null) {
      root = new Node(key, value);
      count = 1;
      return Optional.empty();
    }

    // Clear the path.
    path.clear();

    // Traverse the tree to find a matching key or a new location.
    Node node = root;
    while (node 
  }

  /**
   * @brief Find a value associated with a particular key.
   * @param key key to search for
   * 
   * Since all AVL trees are a subset of binary search trees, we can use the standard method of
   * traversal in order to locate a node. Given the invariant of the self-balancing of the tree, we
   * can assume that the tree has at most log2(n) layers, where n is the number of nodes.
   * <p>
   * Given the aforementioned invariant about the length of the longest path from root to any leaf,
   * we can define the time complexity to be O(lg n) and space complexity to be constant O(1).
   *
   * @return value (if found)
   */
  public Optional<Value> search(final Key key) {
    // Iterate the tree from the root by comparing the needle key to each node's key and select the
    // appropriate direction within the tree.
    Node node = root;
    while (node != null) {
      // Compare the focused node's key to the needle key.
      int cmp = node.key.compareTo(key);
      
      // If the keys are equal.
      if (cmp == 0) {
        return Optional.of(node.value);
      }

      // If the needle key is lesser than the node's key.
      if (cmp < 0) {
        node = node.left;
      }

      // If the needle key is greater than the node's key. 
      if (cmp > 0) {
        node = node.right;
      }
    }

    // Failure to locate the appropriate node results in an empty result.
    return Optional.empty(); 
  }

  /**
   * @brief Remove a particular value (if present) from the tree.
   * @param value value to delete
   *
   * @return presence information
   * @retval true value was present in the tree
   * @retval false value was not present in the tree
   */
  public boolean remove(final T value) {
    return true;
  }

  /**
   * @brief Find the minimal key stored in the tree.
   *
   * This method retrieve the key only, as the associated value is retrievable via the search
   * method, and removable via the remove method.
   * <p>
   * We leverage the knowledge that an AVL tree organises itself so that lesser entries get always
   * stored in the left child link. Therefore, by following left links all the way from the root
   * node we get to the minimal value stored in the tree. The method is mindful of the fact that
   * the tree can be empty, thus having no minimal node. To express this possible failure scenario,
   * the Optional type is utilised.
   * <p>
   * The traversal will access a single node on each layer of the tree, spanning layers from the
   * root node to leafs. Since AVL tree a balanced binary search tree, the length of such path is
   * at most log2(n), where n is the number of all nodes in the tree. As a result of that, the time
   * complexity of the method is O(lg n), space complexity is a constant O(1).
   * 
   * @return minimal key 
   */
  public Optional<Key> minimum() {
    // Early exit for the edge case of an empty tree.
    if (root == null) {
      return Optional.empty();
    }

    // Traverse the tree by following only the left links.
    Node node = root;
    while (node.left != NULL) {
      node = node.left;
    }

    // Wrap the found node in the optional container.
    return Optional.of(node.key);
  }

  /**
   * @brief Find the maximal key stored in the tree.
   *
   * The performance, design decisions and implementations are identical (or analogical) to the
   * {@link #minimum()} method.
   * 
   * @return maximal key 
   */
  public Optional<Key> maximum() {
    // Early exit for the edge case of an empty tree.
    if (root == null) {
      return Optional.empty();
    }

    // Traverse the tree by following only the right links.
    Node node = root;
    while (node.right != NULL) {
      node = node.right;
    }

    // Wrap the found node in the optional container.
    return Optional.of(node.key);
  }

  /**
   * @brief Generate a stream of all keys in the tree.
   *
   * The DFS (depth-first search) algorithm was selected. This has to do with its predictable
   * memory requirements, as opposed to breadth-first search. At any time, the auxiliary memory
   * needs to store at most 2*log2(n) nodes, where n is the overall number of nodes in the tree.
   * <p>
   * Stack implementation from the standard library is used to track the path within the tree. It
   * is not allocated upon each invocation of this method, as that would put memory allocation on
   * the critical path - and possibly cause a huge negative performance impact - but is rather
   * pre-allocated in the constructor method for the instance.
   *
   * @return stream of all keys 
   */
  public Stream<Key> streamKeys() {
    // Early exit in case the tree is empty.
    if (root == null)
      return Stream.empty();    

    // Start the search from the root node.
    path.clear(); 
    path.push(root);

    // Create an empty stream.
    Stream.Builder<Key> stream = Stream.builder();

    while (!path.isEmpty()) {
      // Add the top of the stack to the stream.
      Node top = path.pop();
      stream.accept(top.key);

      // Both links have to be added to the stack. The order does not matter as long as it is
      // consistent.
      if (top.left != null) {
        path.push(top.left);
      }

      if (top.right != null) {
        path.push(top.right);
      }
    }

    return stream.build();
  }

  /**
   * @brief Generate a stream of all values in the tree.
   *
   * @return stream of all values
   */
  public Stream<Value> streamValues() {
    // Early exit in case the tree is empty.
    if (root == null)
      return Stream.empty();    

    // Start the search from the root node.
    path.clear(); 
    path.push(root);

    // Create an empty stream.
    Stream.Builder<Value> stream = Stream.builder();

    while (!path.isEmpty()) {
      // Add the top of the stack to the stream.
      Node top = path.pop();
      stream.accept(top.value);

      // Both links have to be added to the stack. The order does not matter as long as it is
      // consistent.
      if (top.left != null) {
        path.push(top.left);
      }

      if (top.right != null) {
        path.push(top.right);
      }
    }

    return stream.build();
  }

  /**
   * @brief Retrieve the number of values currently present in the tree.
   *
   * The counter value is not re-calculated upon each invocation of this method, but is cached in
   * the instance and appropriately updated upon successful execution of insert and remove methods.
   * Thus, the time and space complexities are both constant O(1).
   *
   * @return number of values in the tree
   * @retval 0 tree is empty
   */
  public long count() {
    return this.count;
  }
}
