package projectus.pus.repository.user;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.entity.user.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
