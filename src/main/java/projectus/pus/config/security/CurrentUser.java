package projectus.pus.config.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymous' ? null : user")
public @interface CurrentUser {
}
