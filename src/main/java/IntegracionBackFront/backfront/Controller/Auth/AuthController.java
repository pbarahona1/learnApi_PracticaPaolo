package IntegracionBackFront.backfront.Controller.Auth;

import IntegracionBackFront.backfront.Entities.Users.UserEntity;
import IntegracionBackFront.backfront.Models.DTO.Users.UserDTO;
import IntegracionBackFront.backfront.Services.Auth.AuthService;
import IntegracionBackFront.backfront.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<String>login (@Valid @RequestBody UserDTO data, HttpServletResponse response){
        if (data.getCorreo() == null || data.getCorreo().isBlank() ||
                data.getContrasena() == null || data.getContrasena().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        if (service.Login(data.getCorreo(), data.getContrasena())){
            addTokenCookie((HttpServletResponse) response, data.getCorreo());
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    private void addTokenCookie(HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UserEntity> userOpt = service.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getId()),
                    user.getCorreo(),
                    user.getTipoUsuario().getNombreTipo() // ← Usar el nombre real del tipo
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }
}
