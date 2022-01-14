package ex.rr.scheduling.service;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import ex.rr.scheduling.EntityMapper;
import ex.rr.scheduling.model.UserEntity;
import ex.rr.scheduling.model.graphql.UserMutation;
import ex.rr.scheduling.repository.CalendarRepository;
import ex.rr.scheduling.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public UserEntity addUser(UserMutation user) {
        return userRepository.save(entityMapper.userMtE(user));
    }

    @Transactional
    public UserEntity updateUserEmail(Integer id, String email) {
        UserEntity user = userRepository.getOne(id);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }


}
