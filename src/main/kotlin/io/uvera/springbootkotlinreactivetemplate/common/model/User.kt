package io.uvera.springbootkotlinreactivetemplate.common.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType

@Document(collection = "users")
class User(
    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    var id: String,
    @Indexed(unique = true)
    var email: String,
    var password: String,
    var active: Boolean,
) {
    var roleSet: MutableSet<UserRole> = mutableSetOf()
}

enum class UserRole {
    ADMIN, USER;

    companion object {
        const val ROLE_PREFIX_VALUE = "ROLE_"
    }
}
