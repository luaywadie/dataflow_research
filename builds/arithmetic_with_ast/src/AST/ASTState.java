package arithmetic.example;

/** @apilevel internal 
 * @ast class
 * @declaredat ASTState:34
 */
public class ASTState extends java.lang.Object {
  
  /**
   * This class stores an attribute value tagged with an iteration ID for
   * a circular evaluation.
   *
   * @apilevel internal
   */
  protected static class CircularValue {
    Object value;
    Cycle cycle;
  }

  

  /**
   * Instances of this class are used to uniquely identify circular evaluation iterations.
   * These iteration ID objects are created for each new fixed-point iteration in
   * a circular evaluation.
   *
   * @apilevel internal
   */
  protected static class Cycle {
  }

  

  /**
   * The iteration ID used outside of circular evaluation.
   *
   * <p>This is the iteration ID when no circular evaluation is ongoing.
   */
  public static final Cycle NON_CYCLE = new Cycle();

  

  /**
   * Tracks the state of the current circular evaluation. This class defines a
   * stack structure where the next element on the stack is pointed to by the
   * {@code next} field.
   *
   * @apilevel internal
   */
  protected static class CircleState {
    final CircleState next;
    boolean change = false;

    /** Evaluation depth of lazy attributes. */
    int lazyAttribute = 0;

    boolean lastCycle = false;

    /** Cycle ID of the latest cycle in this circular evaluation. */
    Cycle cycle = NON_CYCLE;


    protected CircleState(CircleState next) {
      this.next = next;
    }
  }

  


  /** Sentinel circle state representing non-circular evaluation. */
  private static final CircleState CIRCLE_BOTTOM = new CircleState(null);

  

  /**
   * Current circular state.
   * @apilevel internal
   */
  private CircleState circle = CIRCLE_BOTTOM;

  

  /** @apilevel internal */
  protected boolean inCircle() {
    return circle != CIRCLE_BOTTOM;
  }

  

  /** @apilevel internal */
  protected boolean calledByLazyAttribute() {
    return circle.lazyAttribute > 0;
  }

  

  /** @apilevel internal */
  protected void enterLazyAttribute() {
    circle.lazyAttribute += 1;
  }

  

  /** @apilevel internal */
  protected void leaveLazyAttribute() {
    circle.lazyAttribute -= 1;
  }

  

  /** @apilevel internal */
  protected void enterCircle() {
    CircleState next = new CircleState(circle);
    circle = next;
  }

  


  /**
   * Maps circular attribute to last evaluated cycle index.
   * @apilevel internal
   */
  private java.util.Map<Object, Integer> visited = new java.util.IdentityHashMap<Object, Integer>();

  

  /**
   * Check if attribute was already visited during the current cycle.
   * @apilevel internal
   * @return {@code true} if the attribute was already visited.
   */
  protected boolean checkAndSetVisited(Object attribute, int cycle) {
    boolean result = visited.containsKey(attribute) && visited.get(attribute) == cycle;
    visited.put(attribute, cycle);
    return result;
  }

  

  /**
   * Reset visited cycle tracking for this thread.
   * @apilevel internal
   */
  protected void clearVisited() {
    visited.clear();
  }

  

  // TODO(joqvist): may not be necessary.
  /**
   * Reset visit tracker for a single attribute.
   * @apilevel internal
   */
  protected void resetVisited(Object attribute) {
    visited.remove(attribute);
  }

  

  /** @apilevel internal */
  protected void leaveCircle() {
    circle = circle.next;
  }

  

  /** @apilevel internal */
  protected Cycle nextCycle() {
    Cycle cycle = new Cycle();
    circle.cycle = cycle;
    return cycle;
  }

  

  /** @apilevel internal */
  protected Cycle cycle() {
    return circle.cycle;
  }

  

  /** @apilevel internal */
  protected CircleState currentCircle() {
    return circle;
  }

  


  /** @apilevel internal */
  protected void setChangeInCycle() {
    circle.change = true;
  }

  

  /** @apilevel internal */
  protected boolean testAndClearChangeInCycle() {
    boolean change = circle.change;
    circle.change = false;
    return change;
  }

  

  /** @apilevel internal */
  protected boolean changeInCycle() {
    return circle.change;
  }

  

  /** @apilevel internal */
  protected boolean lastCycle() {
    return circle.lastCycle;
  }

  

  /**
   * This is part of the cacheCycle optimization:
   * a circular attribute is evaluated one extra time after the
   * last iteration of Case1, in order to mark all dependencies
   * used in the last iteration as memoized.
   * @apilevel internal
   */
  protected void startLastCycle() {
    nextCycle();
    circle.lastCycle = true;
  }

  

  protected ASTState() {
  }

  public void reset() {
    // Reset circular evaluation state.
    circle = CIRCLE_BOTTOM;
  }


}
