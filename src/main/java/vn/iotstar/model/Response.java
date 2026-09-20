package vn.iotstar.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;

@Schema(description = "Standard API Response Wrapper")
public class Response implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Operation success flag", example = "true")
    private Boolean status;

    @Schema(description = "Informative status message", example = "Operation successful")
    private String message;

    @Schema(description = "Response data payload")
    private Object body;

    public Response() {
    }

    public Response(Boolean status, String message, Object body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public static Response success(String message, Object body) {
        return new Response(true, message, body);
    }

    public static Response error(String message) {
        return new Response(false, message, null);
    }

    public static Response error(String message, Object body) {
        return new Response(false, message, body);
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
