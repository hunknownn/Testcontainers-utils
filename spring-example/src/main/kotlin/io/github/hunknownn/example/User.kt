package io.github.hunknownn.example

import jakarta.persistence.*

@Entity
@Table(name = "users")
open class User(
    name: String,
    email: String
) {
    constructor() : this("", "")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null


    @Column(nullable = false)
    val name = name

    @Column(nullable = false, unique = true)
    val email = email
}