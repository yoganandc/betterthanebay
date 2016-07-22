package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import io.dropwizard.jackson.Jackson;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yoganandc on 7/22/16.
 */
public class UserTest {

    public static final Map<String, User> TEST_USERS = new HashMap<>();
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private User tina;

    static {
        State s1 = new State(23L, "Michigan", "MI");
        Address a1 = new Address(45L, "710 Hawthorne St.", null, "Lincoln Park", s1, "48146");
        State s2 = new State(1L, "Alabama", "AL");
        Address a2 = new Address(46L, "9442 South Middle River St.", null, "Randallstown", s2, "21133");
        State s3 = new State(1L, "Alabama", "AL");
        Address a3 = new Address(47L, "514 Sulphur Springs Rd", null, "Birmingham", s3, "35209");
        SortedSet<Address> addresses = new TreeSet<>();
        addresses.add(a1);
        addresses.add(a2);
        addresses.add(a3);

        State s4 = new State(45L, "Texas", "TX");
        Address a4 = new Address(48L, "7568 Summer Road", null, "Copperas Cove", s4, "76522");
        Payment pay1 = new Payment(25L, "Christine", "Elizabeth", "123456789101112", new Date(1515283200000L), a4, 214);
        State s5 = new State(33L, "New York", "NY");
        Address a5 = new Address(49L, "90 Greenrose Dr", null, "Scarsdale", s5, "10583");
        Payment pay2 = new Payment(26L, "Christopher", "Vivio", "123456789101112", new Date(1546905600000L), a5, 999);
        SortedSet<Payment> payments = new TreeSet<>();
        payments.add(pay1);
        payments.add(pay2);

        Person p1 = new Person(29L, "Tina", null, "Vivio", null);
        TEST_USERS.put("tina", new User(19L, "tinavivio", "$31$16$or3915LByrV-LV07oe9GyXoqaFjiLxMHLuR8NqyYZy4", p1, addresses, payments, null, new Date(1469155469613L), new Date(1469155469613L)));
    }

    @Before
    public void setUp() {
        // USE COPY CONSTRUCTOR SO THAT VARIABLES IN MAP ARE NOT MODIFIED
        tina = new User(UserTest.TEST_USERS.get("tina"));
    }

    @Test
    public void serializeToJSON() throws Exception {
        // WE NEED TO MAKE SURE THAT STRING FORMATTING DOESN'T AFFECT THE RESULT
        // SO WE READ THE FILE, CONVERT TO AN OBJECT, AND WRITE STRING FROM OBJECT
        final User in = MAPPER.readValue(fixture("fixtures/user-tina.json"), User.class);
        final String expected = MAPPER.writeValueAsString(in);

        // COMPARE THAT WRITTEN STRING WITH THE STRING PRODUCED BY TEST INPUT
        assertThat(MAPPER.writeValueAsString(tina)).isEqualTo(expected);
    }

    @Test
    public void deserializeFromJSON() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/user-tina.json"), User.class))
                .isEqualTo(tina);
    }

}