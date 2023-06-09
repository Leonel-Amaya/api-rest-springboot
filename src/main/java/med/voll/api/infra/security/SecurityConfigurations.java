package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Las configuraciones son lo primero en lo que se fija Spring. Lo lee y continua la ejecución con ellas
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
    /*
    Instanciamso un security filter para poder agregarlo como filtro en nuestr filterchain.
    Al haber agregado que dejara libre la ruta /login éste bloquea el paso por nuestro securityfilter
    Así que debemos agregarlo al método para que sea pasado antes de los filtros propios de spring
     */
    @Autowired
    private SecurityFilter securityFilter;

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf->csrf.disable())
                .sessionManagement((sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))
                .build();
    }*/

    /*
    El método establece una autenticación stateless.
    Permite la entrada a la ruta /login a todos los usuarios
    Pero pide autenticación a cualquiera otra ruta
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.sessionManagement(session -> session
//                .sessionCreationPolicy( SessionCreationPolicy.STATELESS ));
//        return httpSecurity.csrf().disable().build();
        httpSecurity.csrf(csrf->csrf.disable())
                .sessionManagement((sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)))//Le indicamos a spring el tipo de sesión
                .authorizeHttpRequests((request -> request.requestMatchers(HttpMethod.POST,"/login")
                        .permitAll()//Los request de tipo post que vayan a login son permitidos, los demás no
                        .anyRequest().authenticated()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /*
    El método se creo para tener un AuthenticationManager
    Que se usará en la clase AutenticacionController
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
    Configuraremos el algoritmo de Hashing que usaremos
    Para que pueda realizar la comparación de las claves
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
