package py.com.fermar.authproviders;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpSession;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    Long getUsuarioId();
    HttpSession getSession();
    void setTrustedToken(String token);
    String getTrustedToken();
}
