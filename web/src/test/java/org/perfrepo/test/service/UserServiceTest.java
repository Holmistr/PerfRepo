package org.perfrepo.test.service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.perfrepo.model.Entity;
import org.perfrepo.model.user.User;
import org.perfrepo.web.dao.DAO;
import org.perfrepo.web.security.SecurityException;
import org.perfrepo.web.service.UserService;
import org.perfrepo.web.service.UserServiceBean;
import org.perfrepo.web.service.exceptions.ServiceException;
import org.perfrepo.web.session.UserSession;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * TODO: document this
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@RunWith(Arquillian.class)
public class UserServiceTest {

    @Inject
    private UserService userService;

    @Deployment
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war");
        war.addPackages(true, DAO.class.getPackage());
        war.addPackages(true, Entity.class.getPackage());
        war.addClasses(UserService.class, UserServiceBean.class);
        war.addPackages(true, UserSession.class.getPackage());
        war.addPackages(true, SecurityException.class.getPackage());
        war.addPackages(true, ServiceException.class.getPackage());
        war.addAsResource("test-persistence.xml", "META-INF/persistence.xml");
        war.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        return war;
    }

    @After
    public void cleanUp() throws Exception {
        for (User user: userService.getAllUsers()) {
            userService.removeUser(user);
        }
    }

    @Test
    public void testUserCRUDOperations() throws Exception {
        User user = new User();
        user.setUsername("test_user");
        fillUser("test_user", user);

        userService.createUser(user);
        Long userId = user.getId();

        User createdUser = userService.getUser(userId);

        assertNotNull(createdUser);
        assertUser(user, createdUser);

        // test properties creation
        Map<String, String> properties = new HashMap<>();
        fillProperties("version_1", "version_1", properties);
        userService.updateUserProperties(properties, user);

        Map<String, String> createdProperties = userService.getUserProperties(createdUser);
        assertEquals(properties, createdProperties);

        // test update
        User userToUpdate = createdUser;
        fillUser("updated_user", createdUser);

        User updatedUser = userService.updateUser(userToUpdate);
        assertUser(userToUpdate, updatedUser);

        // test update properties
        Map<String, String> propertiesToUpdate = new HashMap<>();
        fillProperties("version_1", "updated_version_1", propertiesToUpdate);
        fillProperties("version_2", "new_version_2", propertiesToUpdate);
        userService.updateUserProperties(propertiesToUpdate, user);

        Map<String, String> updatedProperties = userService.getUserProperties(createdUser);
        assertEquals(propertiesToUpdate, updatedProperties);

        // test delete
        User userToDelete = updatedUser;
        userService.removeUser(userToDelete);
        assertNull(userService.getUser(userToDelete.getId()));
    }

    private void assertUser(User expected, User actual) {
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    /**
     * Helper method to assign some values to test user entity.
     *
     * @param prefix
     * @param user
     * @return
     */
    private void fillUser(String prefix, User user) {
        user.setFirstName(prefix + "_first_name");
        user.setLastName(prefix + "_last_name");
        user.setPassword(prefix + "_password");
        user.setEmail(prefix + "_email@example.com");
    }

    private void fillProperties(String keyPrefix, String valuePrefix, Map<String, String> properties) {
        properties.put(keyPrefix + "_key1", valuePrefix + "_value1");
        properties.put(keyPrefix + "_key2", valuePrefix + "_value2");
        properties.put(keyPrefix + "_key3", valuePrefix + "_value3");
    }

}
