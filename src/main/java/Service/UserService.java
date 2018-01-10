/**
 * @author jakubvacek
 */
package Service;

import Core.StatusException;
import Model.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UserService implements UserDetailsService {

    private static final Logger LOG = Logger.getLogger(UserService.class.getName());
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String DATABASE_URL = "jdbc:derby:\\MyDB\\demo;";
    private Dao<User, Integer> dao;

    public UserService() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
            dao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, User.class);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public User getUserById(int userId) throws StatusException {
        try {
            User user = dao.queryForId(userId);
            if (user == null) {
                throw new StatusException(HttpStatus.BAD_REQUEST);
            } else {
                user.setPasswordHash("");
                return user;
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.FORBIDDEN);
        }
    }

    public ArrayList<User> getUsers() throws StatusException {
        ArrayList<User> users = new ArrayList<>();
        try {
            users = (ArrayList<User>) dao.queryForAll();
            for (User u : users) {
                u.setPasswordHash("");
            }
            if (users.isEmpty()) {
                throw new StatusException(HttpStatus.NO_CONTENT);
            } else {
                return users;
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.FORBIDDEN);
        }
    }

    public User getUserByUsername(String username) throws StatusException {
        ArrayList<User> users = new ArrayList<>();
        try {
            users = (ArrayList<User>) dao.queryBuilder().where().eq("username", username).query();
            if (users.isEmpty()) {
                throw new StatusException(HttpStatus.UNAUTHORIZED);
            } else if (users.size() > 1) {
                throw new StatusException(HttpStatus.CONFLICT);
            } else {
                return users.get(0);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDetails userDetails = null;
            User dbUser = this.getUserByUsername(username);
            if (dbUser == null) {
                throw new UsernameNotFoundException("HiMVC Security:: Error in retrieving user(username=" + dbUser.getUsername() + ")");
            }
            userDetails = new org.springframework.security.core.userdetails.User(
                    dbUser.getUsername(),
                    dbUser.getPasswordHash(),//here you can put a clear text password
                    true,
                    true,
                    true,
                    true,
                    Arrays.asList(new SimpleGrantedAuthority(dbUser.getRole()))
            );
            return userDetails;
        } catch (StatusException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new UsernameNotFoundException("HiMVC Security:: Error in retrieving user(username=" + username + ")");
        }
    }
}
