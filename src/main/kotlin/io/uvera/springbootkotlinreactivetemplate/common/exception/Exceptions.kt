package io.uvera.springbootkotlinreactivetemplate.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException


//base
open class BadRequestException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
open class NotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)
