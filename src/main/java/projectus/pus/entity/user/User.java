package projectus.pus.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import projectus.pus.dto.user.UserDto;
import projectus.pus.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.persistence.CascadeType.ALL;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String userName;

    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    public static User of(UserDto.Request requestDto, PasswordEncoder passwordEncoder){

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .userName(requestDto.getUserName())
                .build();
        user.addAuthority(Authority.ofUser(user));

        return user;
    }

    private void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    public List<String> getRoles() {
        return authorities.stream()
                .map(Authority::getRole)
                .collect(toList());
    }

    public void updateData(UserDto.Request requestDto, PasswordEncoder passwordEncoder) {

        if (requestDto.getUserName() != null) {
            this.userName = requestDto.getUserName();
        }

        if (requestDto.getPassword() != null) {
            this.password = passwordEncoder.encode(requestDto.getPassword());
        }

    }


}
