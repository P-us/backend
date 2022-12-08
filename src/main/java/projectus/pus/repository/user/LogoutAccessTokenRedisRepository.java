package projectus.pus.repository.user;

import org.springframework.data.repository.CrudRepository;
import projectus.pus.entity.user.LogoutAccessToken;

public interface LogoutAccessTokenRedisRepository extends CrudRepository<LogoutAccessToken, String> {
}
