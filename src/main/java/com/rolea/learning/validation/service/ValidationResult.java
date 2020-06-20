package com.rolea.learning.validation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResult {

    private List<ValidationWarning> validationWarnings;
    private List<ValidationError> validationErrors;

    public void addValidationWarning(ValidationWarning warning){
        this.validationWarnings.add(warning);
    }

    public void addValidationError(ValidationError error){
        this.validationErrors.add(error);
    }

    public boolean isValid(){
        return isEmpty(validationWarnings) && isEmpty(validationErrors);
    }

}
