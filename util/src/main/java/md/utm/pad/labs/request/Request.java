package md.utm.pad.labs.request;

/**
 * Created by anrosca on Nov, 2017
 */
public class Request {
    private String type;

    public Request() {
    }

    public Request(RequestType requestType) {
        this.type = requestType.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                '}';
    }
}