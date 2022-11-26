package projectus.pus.repository;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.entity.LogoutAccessToken;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
