package io.testcontainers.utils.spring.bootstrap.components

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName


enum class Component {

    POSTGRESQL {
        override fun exec(dockerImageName: DockerImageName): GenericContainer<*> = PostgreSQLContainer(dockerImageName)
    },
//    MONGODB {
//        override fun exec(dockerImageName: DockerImageName): GenericContainer<*> {
//            TODO("Not yet implemented")
//        }
//    },
//    REDIS {
//        override fun exec(dockerImageName: DockerImageName): GenericContainer<*> {
//            TODO("Not yet implemented")
//        }
//    },
//    MINIO {
//        override fun exec(dockerImageName: DockerImageName): GenericContainer<*> {
//            TODO("Not yet implemented")
//        }
//    },
    ;

    abstract fun exec(dockerImageName: DockerImageName): GenericContainer<*>
}