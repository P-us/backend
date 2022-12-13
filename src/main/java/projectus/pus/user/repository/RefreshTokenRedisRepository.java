package projectus.pus.user.repository;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.user.entity.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
