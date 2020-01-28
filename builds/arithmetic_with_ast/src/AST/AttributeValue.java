package arithmetic.example;

/** Wrapper class for storing nullable attribute values. 
 * @ast class
 * @declaredat ASTState:2
 */
public class AttributeValue<T> extends java.lang.Object {
  
  /**
   * This singleton object is an illegal, unused, attribute value.
   * It represents that an attribute has not been memoized, or that
   * a circular attribute approximation has not been initialized.
   */
  public static final Object NONE = new Object();

  

  public final T value;

  

  public AttributeValue(T value) {
    this.value = value;
  }

  

  public static <V> boolean equals(AttributeValue<V> v1, AttributeValue<V> v2) {
    if (v1 == null || v2 == null) {
      return v1 == v2;
    } else {
      return equals(v1.value, v2.value);
    }
  }

  

  public static <V> boolean equals(V v1, V v2) {
    if (v1 == null || v2 == null) {
      return v1 == v2;
    } else {
      return v1 == v2 || v1.equals(v2);
    }
  }


}
