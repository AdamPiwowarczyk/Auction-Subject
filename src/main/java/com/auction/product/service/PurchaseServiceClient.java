package com.auction.product.service;

import com.auction.product.model.PurchaseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "PURCHASE-SERVICE")
@RequestMapping("/purchases")
public interface PurchaseServiceClient {
    @PostMapping
    ResponseEntity<?> addPurchase(@RequestBody PurchaseDto purchase);

    @PostMapping("/buy")
    ResponseEntity<?> buySubjects(@RequestBody List<String> subjects);
}
