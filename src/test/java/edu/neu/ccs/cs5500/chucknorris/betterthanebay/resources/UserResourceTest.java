package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Base64;

import javax.ws.rs.core.HttpHeaders;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.BetterThanEbayAuthenticator;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.UserTest;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final BasicCredentialAuthFilter<User> BASIC_AUTH_HANDLER = new BasicCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new BetterThanEbayAuthenticator(DAO))
            .setPrefix("Basic")
            .setRealm("AUTHENTICATED")
            .buildAuthFilter();

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addProvider(new AuthDynamicFeature(BASIC_AUTH_HANDLER))
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addResource(new UserResource(DAO, ITEM_DAO, BID_DAO, FEEDBACK_DAO))
            .build();

    private User tina;

    @Before
    public void setUp() {
        // USE COPY CONSTRUCTOR SO THAT VARIABLES IN MAP ARE NOT MODIFIED
        tina = new User(UserTest.TEST_USERS.get("tina"));
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void testGetUser() {
        when(DAO.findByCredentials("tinavivio", "password5")).thenReturn(tina);
        when(DAO.findById(19L)).thenReturn(tina);

        User found = RULE.getJerseyTest().target("/users/19").request()
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("tinavivio:password5".getBytes()))
                .get(User.class);

        assertThat(found).isEqualTo(tina);
        verify(DAO).findByCredentials("tinavivio", "password5");
        verify(DAO).findById(19L);
    }
}