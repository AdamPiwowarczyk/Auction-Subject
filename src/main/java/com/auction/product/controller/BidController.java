package com.auction.product.controller;

import com.auction.product.service.SubjectService;
import lombok.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class BidController {
    private final SubjectService subjectService;

    @MessageMapping("/hello/{code}")
    @SendTo("/topic/greetings/{code}")
    public StackDto handleMessage(StackDto stackDto) {
//        Double updatedPrice = stackDto.getIncrementalAmount() + stackDto.getCurrentPrice();
//        Double updatedStack = subjectService.getBid(updatedPrice);
//
//        return new StackDto(updatedStack, updatedPrice, stackDto.getUsername());
        Double currentPrice = stackDto.getCurrentPrice();
        Double incrementalAmount = subjectService.getIncrementalAmount(currentPrice);
        return new StackDto(currentPrice + incrementalAmount, currentPrice, stackDto.getUsername());
    }
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class StackDto {
    private Double minPrice;
    private Double currentPrice;
    private String username;
}
