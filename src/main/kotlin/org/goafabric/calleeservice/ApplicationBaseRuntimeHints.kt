package org.goafabric.calleeservice

import org.springframework.aot.hint.*
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import java.util.function.Consumer

@Configuration
@ImportRuntimeHints(ApplicationBaseRuntimeHints::class)
class ApplicationBaseRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        //springdoc
        hints.reflection().registerType(
            TypeReference.of("org.springdoc.core.providers.SpringWebProvider$\$SpringCGLIB$$0"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withField("CGLIB\$FACTORY_DATA") })

        hints.reflection().registerType(
            TypeReference.of("org.springdoc.core.providers.SpringWebProvider$\$SpringCGLIB$$0"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withField("CGLIB\$CALLBACK_FILTER") })

        hints.reflection().registerType(
            TypeReference.of("org.springdoc.core.providers.SpringWebProvider"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })

        //kafka
        hints.reflection().registerType(
            TypeReference.of("java.security.AccessController"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })

        hints.reflection().registerType(
            TypeReference.of("javax.security.auth.Subject"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })

        hints.reflection().registerType(
            TypeReference.of("org.apache.kafka.common.security.oauthbearer.DefaultJwtRetriever"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })

        hints.reflection().registerType(
            TypeReference.of("org.apache.kafka.common.security.oauthbearer.DefaultJwtValidator"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })

        //adapter
        hints.reflection().registerType(
            TypeReference.of("org.springframework.web.client.ResourceAccessException"),
            Consumer { builder: TypeHint.Builder? -> builder!!.withMembers(MemberCategory.INVOKE_DECLARED_METHODS) })
    }
}
