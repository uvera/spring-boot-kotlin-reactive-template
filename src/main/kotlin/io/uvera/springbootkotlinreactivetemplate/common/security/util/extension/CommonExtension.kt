package io.uvera.springbootkotlinreactivetemplate.common.security.util.extension

import io.uvera.springbootkotlinreactivetemplate.common.exception.ObjectErrorCompact
import org.springframework.validation.ObjectError

/*
 * BindingResult's ObjectError in a compact form
 */
val List<ObjectError>.compact: List<ObjectErrorCompact>
    get() = this.map {
        ObjectErrorCompact(it.defaultMessage ?: "Unknown error", it.code ?: "UnknownException")
    }
