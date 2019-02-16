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
public final class Tree<T extends Comparable<T>> {

  private final class Node {
    public Optional<Node> left;
    public Optional<Node> right;
    public T value;
  }

  private long length;
  private Optional<Node> root;

  /**
   * @brief Initialise an empty tree.
   */
  public Tree() {
    length = 0;
  }
  
  /**
   * @brief Add a value to the tree, possibly overwriting an existing one.
   * @param value value to insert
   *
   * @return presence information
   * @retval true value was present in the tree
   * @retval false value was not present in the tree
   */
  public boolean insert(final T value) {
    return true;
  }

  /**
   * @brief Verify whether a particular value is already present within the tree.
   * @param value value to search for
   *
   * @return presence information
   * @retval true value is present in the tree
   * @retval false value is not present in the tree
   */
  public boolean search(final T value) {
    return true;
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
   * @brief Find the smallest value stored in the tree.
   * 
   * @return smallest value
   */
  public Optional<T> minimum() {
    return Optional.empty();
  }

  /**
   * @brief Find the greatest value stored in the tree.
   *
   * @return greatest value
   */
  public Optional<T> maximum() {
    return Optional.empty();
  }

  /**
   * Generate a stream of all values in the tree.
   *
   * @return stream of all values
   */
  public Stream<T> stream() {
    return Stream.empty();    
  }

  /**
   * @brief Compute the number of values currently present in the tree.
   *
   * @return number of values in the tree
   * @retval 0 tree is empty
   */
  public long length() {
    return this.length;
  }
}
