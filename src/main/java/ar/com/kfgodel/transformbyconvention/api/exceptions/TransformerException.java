package ar.com.kfgodel.transformbyconvention.api.exceptions;

/**
 * This type represents an error on transformer definitions
 * Created by kfgodel on 20/03/16.
 */
public class TransformerException extends RuntimeException {

  public TransformerException(String message) {
    super(message);
  }

  public TransformerException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransformerException(Throwable cause) {
    super(cause);
  }
}
