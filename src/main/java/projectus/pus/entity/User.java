package projectus.pus.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private String userName;

    public User(Long id, String email, String password, String userName){
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
    }
}
