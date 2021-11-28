package com.auction.product.controller;

import com.auction.product.model.StackDto;
import com.auction.product.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidController {
    private final SubjectService subjectService;

    @MessageMapping("/message/{code}")
    @SendTo("/auction/bid/{code}")
    public StackDto handleMessage(StackDto stackDto) {
        Double currentPrice = stackDto.getCurrentPrice();
        Double incrementalAmount = subjectService.getIncrementalAmount(currentPrice);
        return new StackDto(currentPrice + incrementalAmount, currentPrice, stackDto.getUsername());
    }
}
