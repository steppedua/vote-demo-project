package com.steppedua.loadbalancer.controller.validator;


import com.steppedua.loadbalancer.model.VoteSaveRequestDto;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class VoteSaveValidator implements Validator {
    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return VoteSaveRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        VoteSaveRequestDto request = (VoteSaveRequestDto) target;
        if (request.getVoteValue() == null) {
            errors.rejectValue("voteValue", "field.require", "Не указан тип voteValue");
        }
    }
}
