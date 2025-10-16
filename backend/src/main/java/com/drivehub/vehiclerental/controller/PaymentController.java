package com.drivehub.vehiclerental.controller;

import com.drivehub.vehiclerental.dto.PaymentRequest;
import com.drivehub.vehiclerental.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for payment processing endpoints
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment processing APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/process")
    @Operation(summary = "Process payment", description = "Process payment for a booking using strategy pattern")
    public ResponseEntity<Map<String, String>> processPayment(@Valid @RequestBody PaymentRequest request) {
        String result = paymentService.processPayment(request);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }
}
