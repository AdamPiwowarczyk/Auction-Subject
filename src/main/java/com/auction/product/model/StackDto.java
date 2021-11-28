package com.auction.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StackDto {
    private Double minPrice;
    private Double currentPrice;
    private String username;
}
