package egovframework.com.fivemlist.exception;

public class InvalidTokenException extends RuntimeException {

    // 기본 생성자
    public InvalidTokenException(String message) {
        super(message);  // 예외 메시지를 부모 클래스에 전달
    }

    // 예외 메시지와 원인(예외)을 함께 전달할 수 있는 생성자
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);  // 메시지와 원인을 부모 클래스에 전달
    }
}
