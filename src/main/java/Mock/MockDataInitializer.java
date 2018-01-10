/**
 * @author jakubvacek
 */
package Mock;

import Model.User;
import Model.Reservation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MockDataInitializer {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String DATABASE_URL = "jdbc:derby:\\MyDB\\demo;create=true";
    private Dao<Reservation, Integer> resevationDao;
    private Dao<User, Integer> userDao;

    public MockDataInitializer() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
            resevationDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, Reservation.class);
            userDao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, User.class);

            setUpDatabase(connectionSource);
            dummyData();
        } catch (SQLException ex) {
            Logger.getLogger(MockDataInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void setUpDatabase(ConnectionSource connectionSource) throws SQLException {
        //user
        TableUtils.dropTable(userDao, true);
        TableUtils.createTable(connectionSource, User.class);
        //resevation
        TableUtils.dropTable(resevationDao, true);
        TableUtils.createTable(connectionSource, Reservation.class);
    }

    public final void dummyData() throws SQLException {
        User admin = new User(0, "Admin", "admin", "ADMIN");
        this.createUser(admin);
        User user = new User(0, "User", "user", "USER");
        this.createUser(user);
    }

    public int createUser(User user) throws SQLException {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        System.out.println(user);
        return userDao.create(user);
    }

}
