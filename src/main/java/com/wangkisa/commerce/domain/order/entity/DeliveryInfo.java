package com.wangkisa.commerce.domain.order.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
public class DeliveryInfo {

    private String receiverName;
    private String receiverAddress;
    private String etcMessage;

    @Builder
    public DeliveryInfo(String receiverName, String receiverAddress, String etcMessage) {
        this.receiverName = receiverName;
        this.receiverAddress = receiverAddress;
        this.etcMessage = etcMessage;
    }
}
