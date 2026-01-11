package com.cathay.identify.service;

import com.cathay.identify.dto.request.account.AccountCreationRequest;
import com.cathay.identify.dto.request.account.AccountUpdateRequest;
import com.cathay.identify.dto.request.account.ChangePasswordRequest;
import com.cathay.identify.entity.AccountEntity;
import com.cathay.identify.exception.AppException;
import com.cathay.identify.exception.ErrorCode;
import com.cathay.identify.interfaces.AccountService;
import com.cathay.identify.mapper.AccountMapper;
import com.cathay.identify.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accRepo;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    @Override
    public AccountEntity createAccount(AccountCreationRequest dto) {
        val emailExist = accRepo.existsByEmail(dto.getEmail());
        if (emailExist){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        val hash_password = passwordEncoder.encode(dto.getPassword());
        AccountEntity acc = AccountEntity.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .hash_password(hash_password)
                .build();
        return accRepo.save(acc);
    }

    @Override
    public List<AccountEntity> getAccounts() {
        return accRepo.findAll();
    }

    @Override
    public Optional<AccountEntity> getAccountById(String id) {
        return accRepo.findById(id);
    }

    @Override
    public Optional<AccountEntity> getAccountByEmail(String email) {
        return accRepo.findAccountsByName(email);
    }

    @Override
    public AccountEntity updateAccount(String id, AccountUpdateRequest dto) {
        val acc = accRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        accountMapper.updateAccount(acc, dto);
        return accRepo.save(acc);
    }

    @Override
    public AccountEntity changePassword(String id, ChangePasswordRequest dto) {
        val acc = accRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        val hash_cur_pass = passwordEncoder.encode(dto.getCurrent_pass());
        if (hash_cur_pass.equals(acc.getHash_password())){
            val hash_new_pass = passwordEncoder.encode(dto.getNew_pass());
            acc.setHash_password(hash_new_pass);
            return accRepo.save(acc);
        }
        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }
}
