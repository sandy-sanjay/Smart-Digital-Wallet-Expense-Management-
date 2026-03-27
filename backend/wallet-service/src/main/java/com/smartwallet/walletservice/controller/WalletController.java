package com.smartwallet.walletservice.controller;

import com.smartwallet.walletservice.entity.Wallet;
import com.smartwallet.walletservice.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/{userId}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getWalletByUserId(userId));
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<Wallet> addFunds(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(walletService.addFunds(userId, amount));
    }

    @PostMapping("/{userId}/deduct")
    public ResponseEntity<?> deductFunds(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        try {
            return ResponseEntity.ok(walletService.deductFunds(userId, amount));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
