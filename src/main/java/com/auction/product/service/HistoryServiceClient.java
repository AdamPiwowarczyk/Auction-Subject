package com.auction.product.service;

import com.auction.product.configuration.FeignClientConfiguration;
import com.auction.product.model.PurchaseDto;
import com.auction.product.model.Subject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "HISTORY-SERVICE", configuration = FeignClientConfiguration.class)
@RequestMapping("/history")
public interface HistoryServiceClient {
    //    @PostMapping
//    PurchaseDto addPurchase(@RequestBody PurchaseDto purchase);
    @PostMapping
    ResponseEntity<?> addPurchase(@RequestHeader(name = "Authorization") String token, @RequestBody PurchaseDto purchase);

    @PostMapping("/buy")
    ResponseEntity<?> buySubjects(@RequestBody List<String> subjects);
//    @PostMapping("/buy")
//    ResponseEntity<?> buySubjects(@RequestHeader(name = "Authorization") String token, @RequestBody List<String> subjects);
}
