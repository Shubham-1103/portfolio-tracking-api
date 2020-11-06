package com.shubham.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class TradeValidation {
    boolean isValid;
    ResponseEntity<?> responseEntity;
}
