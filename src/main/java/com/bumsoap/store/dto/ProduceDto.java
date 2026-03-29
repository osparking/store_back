package com.bumsoap.store.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import static com.bumsoap.store.util.BsUtils.getShortTimeStr;

@Data
@NoArgsConstructor
public class ProduceDto {
    private Long id;
    private String shape;
    private Long quantity;
    private String produceDate;
    private Long producerId;
    private String producerName;
    private String registerName;
    private String registerTime;

    public ProduceDto(ProducePageRow row) {
        this.id = row.getId();
        this.shape = row.getBsShape().toString();
        this.quantity = row.getQuantity();
        this.produceDate = row.getProduceDate().toString();
        this.producerId = row.getProducerId();
        this.producerName = row.getProducerName();
        this.registerName = row.getRegister();
        this.registerTime = getShortTimeStr(row.getRegisterTime());
    }
}
