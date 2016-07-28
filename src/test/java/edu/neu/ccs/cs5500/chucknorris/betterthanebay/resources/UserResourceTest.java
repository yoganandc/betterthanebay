package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.*;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.BetterThanEbayAuthenticator;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.PasswordUtil;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by yoganandc on 7/21/16.
 */
public class UserResourceTest {

    private final static UserDAO DAO = mock(UserDAO.class);
    private static final ItemDAO ITEM_DAO = mock(ItemDAO.class);
    private static final BidDAO BID_DAO = mock(BidDAO.class);
    private static final FeedbackDAO FEEDBACK_DAO = mock(FeedbackDAO.class);

    private static final PasswordUtil util = new PasswordUtil();

    private static final BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER = new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new BetterThanEbayAuthenticator(DAO, util))
            .setPrefix("Basic")
            .setRealm("AUTHENTICATED")
            .buildAuthFilter();

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addResource(new UserResource(DAO, ITEM_DAO, BID_DAO, FEEDBACK_DAO, util))
            .build();

    private User tina;
    private User sully;

    @Before
    public void setUp() {
        // USE COPY CONSTRUCTOR SO THAT VARIABLES IN MAP ARE NOT MODIFIED
        tina = new User(UserTest.TEST_USERS.get("tina"));
        sully = new User(UserTest.TEST_USERS.get("sully"));

    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void testGetUser() {
        when(DAO.findByCredentials("tinavivio")).thenReturn(tina);
        when(DAO.findById(19L)).thenReturn(tina);

        User found = RULE.getJerseyTest().target("/users/19").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("tinavivio:password5".getBytes()))
                .get(User.class);

        assertThat(found).isEqualTo(tina);
        verify(DAO).findByCredentials("tinavivio");
        verify(DAO).findById(19L);
    }


    @Test
    public void searchByUsername() {
        when(DAO.findByCredentials("tinavivio")).thenReturn(tina);
        List<User> userListTina = new ArrayList<>();
        userListTina.add(tina);
        when(DAO.searchByUsername("tinavivio", 0, 20)).thenReturn(userListTina);

        List<User> found = RULE.getJerseyTest().target("/users")
                .queryParam("username", "tinavivio")
                .request()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("tinavivio:password5".getBytes()))
                .get(new GenericType<List<User>>(){});

        assertThat(found).isEqualTo(userListTina);
        verify(DAO).findByCredentials("tinavivio");
        verify(DAO).searchByUsername("tinavivio", 0, 20);
    }

    @Test
    public void addUser() {
        State stateCA = new State(2L, "California", "CA");
        Address sullyAddress = new Address(null, "1313 Disneyland Dr", null, "Anaheim", stateCA, "92802");
        SortedSet<Address> sullyAddresses = new TreeSet<>();
        sullyAddresses.add(sullyAddress);

        Payment sullyPay = new Payment(null, "James", "Sullivan", "123456789109887",
                new Date(1546905600000L), sullyAddress, 999);
        SortedSet<Payment> sullyPayments = new TreeSet<>();
        sullyPayments.add(sullyPay);

        Person person2 = new Person(null, "Sully", null, "Sullivan", null);
        User sully2 = new User(null, "jsully", "disney",
                person2, sullyAddresses,
                sullyPayments,
                null, null, null);
        when(DAO.create(sully2)).thenReturn(sully);

        User createdSully = RULE.getJerseyTest().target("/users").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(sully2, MediaType.APPLICATION_JSON), User.class);

        assertThat(createdSully).isEqualTo(sully);
        verify(DAO).create(sully2);


    }


}