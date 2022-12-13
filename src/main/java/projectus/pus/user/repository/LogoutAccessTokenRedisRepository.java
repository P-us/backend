package projectus.pus.user.repository;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.user.entity.LogoutAccessToken;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
