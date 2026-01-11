package com.cathay.identify.interfaces;

import com.cathay.identify.dto.request.account.AccountCreationRequest;
import com.cathay.identify.dto.request.account.AccountUpdateRequest;
import com.cathay.identify.dto.request.account.ChangePasswordRequest;
import com.cathay.identify.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountEntity createAccount (AccountCreationRequest dto);

    List<AccountEntity> getAccounts ();

    Optional<AccountEntity> getAccountById (String id);

    Optional<AccountEntity> getAccountByEmail (String email);

    AccountEntity updateAccount (String id, AccountUpdateRequest dto);

    AccountEntity changePassword (String id, ChangePasswordRequest dto);
}
