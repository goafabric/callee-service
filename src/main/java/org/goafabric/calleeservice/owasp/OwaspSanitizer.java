/*
package org.goafabric.calleeservice.owasp;

import jakarta.annotation.PostConstruct;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(OwaspSanitizer.ReflectionRuntimeHints.class)
//uses: implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20240325.1")
public class OwaspSanitizer {
    @PostConstruct
    public void init() {
        String unsafeHtml = "<script>alert('XSS');</script><p>Hello <b>world</b>!</p>";
        // Use a prebuilt sanitizer policy (HTML5 + formatting + links + styling)
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS).and(Sanitizers.BLOCKS);
        System.out.println(policy.sanitize(unsafeHtml));
    }


    static class ReflectionRuntimeHints implements RuntimeHintsRegistrar {

        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            try {
                //for the simple usecase above at least this hint is required
                hints.reflection().registerType(Class.forName("org.owasp.shim.ForJava9AndLater"), MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

 */
