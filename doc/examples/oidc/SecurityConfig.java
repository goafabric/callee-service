//not required for testing, only for production to toggle actuator endpoints and security on/off
@Bean
public SecurityFilterChain filterChain(HttpSecurity http, @Value("${security.authentication.enabled:true}") Boolean isAuthenticationEnabled, HandlerMappingIntrospector introspector) throws Exception {
    return isAuthenticationEnabled
            ? http.authorizeHttpRequests(auth -> auth.requestMatchers(new MvcRequestMatcher(introspector, "/actuator/**")).permitAll().anyRequest().authenticated())
            //.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER))
            .oauth2Login(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).build()
            : http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).httpBasic(httpBasic -> {}).csrf(AbstractHttpConfigurer::disable).build();
}