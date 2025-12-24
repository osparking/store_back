package com.bumsoap.store.track;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookBody {
    private String id;
    private String tracking_number;
    private String courier_code;
    private String order_number;
    private String delivery_status;

    @Override
    public String toString() {
        return "WebhookBody{" +
                "id='" + id + '\'' +
                ", tracking_number='" + tracking_number + '\'' +
                ", courier_code='" + courier_code + '\'' +
                ", order_number='" + order_number + '\'' +
                ", delivery_status='" + delivery_status + '\'' +
                '}';
    }
}
