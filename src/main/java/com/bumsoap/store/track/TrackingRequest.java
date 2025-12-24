package com.bumsoap.store.track;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackingRequest {
    private int code;
    private String message;
    private WebhookBody data;

    @Override
    public String toString() {
        return "TrackingRequest{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
