package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainValues

@DomainValues
public enum class Sex(value: Int) {
    MALE(0), FEMAIL(1), UNDEFINED(2)
}
