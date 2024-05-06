@Bean
public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") Boolean isAuthenticationEnabled, HandlerMappingIntrospector introspector) throws Exception {
    return isAuthenticationEnabled
            ? http.authorizeHttpRequests(auth -> auth.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll().anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).build()
            : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build();
}