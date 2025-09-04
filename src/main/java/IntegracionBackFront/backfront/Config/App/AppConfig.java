package IntegracionBackFront.backfront.Config.App;

import IntegracionBackFront.backfront.Utils.JWTUtils;
import IntegracionBackFront.backfront.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils){
        return new JwtCookieAuthFilter(jwtUtils);
    }
}
