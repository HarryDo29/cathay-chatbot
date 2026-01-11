package com.cathay.identify.mapper;

import com.cathay.identify.dto.request.account.AccountUpdateRequest;
import com.cathay.identify.entity.AccountEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccount(@MappingTarget AccountEntity user, AccountUpdateRequest request);
}
