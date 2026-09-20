package vn.iotstar.exception;

public class CategoryNotEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CategoryNotEmptyException(String message) {
        super(message);
    }
}
