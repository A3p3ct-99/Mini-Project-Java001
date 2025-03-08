package org.example.functional;


import org.example.validation.ValidationResult;

@FunctionalInterface
public interface ValidationPredicate {
    ValidationResult test(String value);
}


