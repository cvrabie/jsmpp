/*
 * User: CVrabie1
 * Date: 31/10/11
 */

package org.jsmpp.session.state;

public class Receipt {

  public static final int DEFAULT_ERROR_CODE = 0x64;;

  public static enum ResponseType{
    /** Signals that everything went ok **/
    SUCCESS,
    /** Signals that some error occurred while processing **/
    FAILURE,
    /** Signals that everything went ok so far but there's something that's still
     * being processed asynchronously **/
    CONTINUE
  }

  public final ResponseType type;
  public final Throwable cause;

  public static final Receipt SUCCESS = new Receipt(ResponseType.SUCCESS);
  public static final Receipt FAILURE = new Receipt(ResponseType.FAILURE);
  public static final Receipt CONTINUE = new Receipt(ResponseType.CONTINUE);

  public static Receipt FAILURE(final Throwable cause) {
    return new Receipt(ResponseType.FAILURE, cause);
  }

  public static Receipt FAILURE_DESTINATION_IS_OFFLINE(String username) {
    return FAILURE(new IllegalStateException("Cannot deliver message to user "+username+" because he's offline!"));
  }

  public static final Receipt FAILURE_CANT_HANDLE_THIS_TYPE(Class clazz){
    return FAILURE(new IllegalArgumentException("Cannot handle messages of type "+clazz.getName()));
  }

  public Receipt(final ResponseType type) {
    this(type, null);
  }

  public Receipt(final ResponseType type, final Throwable cause) {
    this.type = type;
    this.cause = cause;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Receipt response = (Receipt) o;

    if (type != response.type) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return type != null ? type.hashCode() : 0;
  }

  @Override
  public String toString() {
    return type.toString();
  }
}