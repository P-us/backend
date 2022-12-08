package projectus.pus.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String role;

    public static Authority ofUser(User user) {
        return Authority.builder()
                .role("ROLE_USER")
                .user(user)
                .build();
    }

    public static Authority ofAdmin(User user) {
        return Authority.builder()
                .role("ROLE_ADMIN")
                .user(user)
                .build();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
