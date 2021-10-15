package io.uvera.springbootkotlinreactivetemplate.common.repository

import io.uvera.springbootkotlinreactivetemplate.common.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByEmail(email: String): Mono<User>
    fun existsByEmail(email: String): Mono<Boolean>
}

@Component
class UserModelListener : AbstractMongoEventListener<User>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<User>) {
        if (event.source.id == "")
            event.source.id = ObjectId().toString()
    }
}
