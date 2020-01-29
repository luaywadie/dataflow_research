/**
 * @ast class
 * @declaredat ASTNode:214
 */
public class ASTNodeAnnotation extends java.lang.Object {
  
  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface Child {
    String name();
  }

  
  
  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface ListChild {
    String name();
  }

  
  
  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface OptChild {
    String name();
  }

  
  
  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface Token {
    String name();
  }

  
  
  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface Attribute {
    Kind kind();
    boolean isCircular() default false;
    boolean isNTA() default false;
  }

  
  public enum Kind { SYN, INH, COLL }

  

  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
  @java.lang.annotation.Documented
  public @interface Source {
    String aspect() default "";
    String declaredAt() default "";
  }

  

  @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
  @java.lang.annotation.Target(java.lang.annotation.ElementType.CONSTRUCTOR)
  @java.lang.annotation.Documented
  public @interface Constructor{
    String[] name(); 
    String[] type(); 
    String[] kind(); 
  }


}
