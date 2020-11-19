package test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PasswordEncoderTest {

    @Test
    public void producesHashFromPlaintext() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        String hash = encoder.encode("plaintext");

        assertThat(hash, startsWith("$2a$10"));
        assertThat(hash.length(), is(60));
    }

}
