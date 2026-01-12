package com.cathay.identify.controller;

import com.cathay.identify.dto.request.account.AccountCreationRequest;
import com.cathay.identify.dto.request.account.AccountUpdateRequest;
import com.cathay.identify.dto.request.account.ChangePasswordRequest;
import com.cathay.identify.dto.response.ApiResponse;
import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.exception.AppException;
import com.cathay.identify.exception.ErrorCode;
import com.cathay.identify.repository.AccountRepository;
import com.cathay.identify.service.AccountServiceImpl;
import com.cathay.identify.security.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accSer;
    private final AccountRepository accRepo;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<AccountEntity> createAccount (@RequestBody @Valid AccountCreationRequest reqBody){
        return ApiResponse.<AccountEntity>builder()
                .result(accSer.createAccount(reqBody))
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<AccountEntity>> getAccounts (){
        return ApiResponse.<List<AccountEntity>>builder()
                .result(accSer.getAccounts())
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<AccountEntity> getAccountById(){
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        val account = accRepo.findAccountByEmail(authentication.getPrincipal().toString());
        if (account.isPresent())
            return ApiResponse.<AccountEntity>builder()
                    .result(account.get())
                    .build();
        else throw new AppException(ErrorCode.USER_NOT_FOUND);
    }

    @GetMapping("/{email}")
    public ApiResponse<AccountEntity> getAccountByEmail(@PathVariable String email){
        val account = accSer.getAccountByEmail(email);
        if (account.isPresent())
            return ApiResponse.<AccountEntity>builder()
                    .result(account.get())
                    .build();
        else throw new AppException(ErrorCode.USER_NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{account_id}")
    public ApiResponse<AccountEntity> updateAccount(@PathVariable String account_id, @RequestBody AccountUpdateRequest updateDto){
        return ApiResponse.<AccountEntity>builder()
                .result(accSer.updateAccount(account_id, updateDto))
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<AccountEntity> changePassword(@RequestBody ChangePasswordRequest changePassDto){
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AccountEntity account = accRepo.findAccountByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new UsernameNotFoundException("Account not exist"));
        return ApiResponse.<AccountEntity>builder()
                .result(accSer.changePassword(account.getId(), changePassDto))
                .build();
    }
}
