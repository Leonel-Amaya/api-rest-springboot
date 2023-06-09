package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
//        if (authHeader == null || authHeader == "") {
////            throw new RuntimeException("El authHeader enviado no es válido");
////        }
        // Revertimos el proceso y mejor se valida si el authHeader no es nulo
        if (authHeader != null) {
            var token = authHeader.replace("Bearer ", "");
            var nombreUsuario = tokenService.getSubject(token); // Extraemos el nombre de usuario
            if (nombreUsuario != null) {
                // Token válido
                var usuario = usuarioRepository.findByLogin(nombreUsuario);// Encontramos al usuario
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                        usuario.getAuthorities()); // Forzamos un inicio de sesión
                SecurityContextHolder.getContext().setAuthentication(authentication); // Setea manualmente en el securitycontext
                // de spring esa autenticacion
                // de modo que para los demás request el usuario ya va a estar autenticado

            }
        }
        filterChain.doFilter(request, response);
    }
}
