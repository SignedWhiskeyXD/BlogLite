package top.whiskeyxd.blogliteauth.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import top.whiskeyxd.blogliteauth.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
