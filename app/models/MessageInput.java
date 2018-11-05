package models;

public class MessageInput
{
    public MessageInput(String message) {
        this.message = message;
    }

    public MessageInput() {
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
