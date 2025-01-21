package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainValues
import kotlinx.serialization.Serializable

/**
 * Represents the biological sex of an individual.
 */
@Serializable
@DomainValues
public enum class Sex(value: Int) {
    /** Represents the male biological sex. */
    MALE(0),

    /** Represents the female biological sex. */
    FEMAIL(1),

    UNDEFINED(2)
}
