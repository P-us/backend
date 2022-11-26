package projectus.pus.repository;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.entity.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
