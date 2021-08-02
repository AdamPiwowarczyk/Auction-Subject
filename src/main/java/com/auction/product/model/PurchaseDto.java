package com.auction.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseDto {
    private String code;
    private Double price;
    private String username;
}
