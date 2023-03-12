package py.com.fermar.authproviders;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Service
public class AuthenticationFacade implements IAuthenticationFacade {
    private static final String TRUSTED_TOKEN = "TRUSTED_TOKEN";

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public Long getUsuarioId() {
        return Long.valueOf(getAuthentication().getName());
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public void setTrustedToken(String token) {
        getSession(true).setAttribute(TRUSTED_TOKEN, token);
    }

    private HttpSession getSession(boolean editable) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(editable);
    }

    public String getTrustedToken() {
        String retorno = null;
        Object trustedToken = getSession(false).getAttribute(TRUSTED_TOKEN);
        if (trustedToken != null) {
            retorno = (String) trustedToken;
        }
        return retorno;
    }
}
