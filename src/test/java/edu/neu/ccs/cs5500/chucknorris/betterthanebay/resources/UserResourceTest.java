package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.HttpHeaders;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.BetterThanEbayAuthenticator;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.PasswordUtil;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Address;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Person;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
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
        State s1 = new State(23L, "Michigan", "MI");
        Address a1 = new Address(45L, "710 Hawthorne St.", null, "Lincoln Park", s1, "48146");
        State s2 = new State(1L, "Alabama", "AL");
        Address a2 = new Address(46L, "9442 South Middle River St.", null, "Randallstown", s2, "21133");
        State s3 = new State(1L, "Alabama", "AL");
        Address a3 = new Address(47L, "514 Sulphur Springs Rd", null, "Birmingham", s3, "35209");
        Set<Address> addresses = new HashSet<>();
        addresses.add(a1);
        addresses.add(a2);
        addresses.add(a3);

        State s4 = new State(45L, "Texas", "TX");
        Address a4 = new Address(48L, "7568 Summer Road", null, "Copperas Cove", s4, "76522");
        Payment pay1 = new Payment(25L, "Christine", "Elizabeth", "123456789101112", new Calendar.Builder().setDate(2018, 1, 7).build().getTime(), a4, 214);
        State s5 = new State(33L, "New York", "NY");
        Address a5 = new Address(49L, "90 Greenrose Dr", null, "Scarsdale", s5, "10583");
        Payment pay2 = new Payment(26L, "Christopher", "Vivio", "123456789101112", new Calendar.Builder().setDate(2019, 1, 8).build().getTime(), a5, 999);
        Set<Payment> payments = new HashSet<>();
        payments.add(pay1);
        payments.add(pay2);

        Person p1 = new Person(29L, "Tina", null, "Vivio", null);
        tina = new User(19L, "tinavivio", new PasswordUtil().hash("password5".toCharArray()), p1, addresses, payments, null, new Date(1469151674635L), new Date(1469151674635L));
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