package com.tsp.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class PasswordEncoderTest {
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
        passwordEncoder = createDelegatingPasswordEncoder();
    }

    @Test
    void delegatingPasswordEncoding() {
        // given
        String password = "pass1234";
        // when
        String encodedPassword = passwordEncoder.encode(password);
        // then
        assertThat(passwordEncoder.matches(password, encodedPassword)).isTrue();
    }

    @Test
    void defaultDelegatingPasswordEncoder() {
        String encodedPassword = passwordEncoder.encode("pass1234");
        assertThat(encodedPassword).startsWith("{bcrypt}");
    }

    @Test
    void match() {
        String password = "password";
        String encPassword1 = "{pbkdf2}7a07c208fc2a407fb89cc3b6effb1b759da575a85f65dda9cd426f1ad14b56e6afaeeea6f9269569";
        String encPassword2 = "{bcrypt}$2a$10$Ot44NE6k1kO5bfNHTP0m8ejdpGr8ooHGT90lOD2/LpGIzfiS3p6oq";
        String encPassword3 = "{sha256}fcef9e3f82af42d9059e74a95c633fe99b7aba1c4bfb9ac1cae31dd1b67060da933776fee8baec8f";

        assertThat(passwordEncoder.matches(password, encPassword1)).isTrue();
        assertThat(passwordEncoder.matches(password, encPassword2)).isTrue();
        assertThat(passwordEncoder.matches(password, encPassword3)).isTrue();
    }

    @Test
    void 사용한_비밀번호변환기_접두사가없으면오류발생() {
        String password = "password";
        String encPassword1 = "7a07c208fc2a407fb89cc3b6effb1b759da575a85f65dda9cd426f1ad14b56e6afaeeea6f9269569";   // pbkdf2
        String encPassword2 = "$2a$10$Ot44NE6k1kO5bfNHTP0m8ejdpGr8ooHGT90lOD2/LpGIzfiS3p6oq";                       // bcrypt
        String encPassword3 = "fcef9e3f82af42d9059e74a95c633fe99b7aba1c4bfb9ac1cae31dd1b67060da933776fee8baec8f";   // sha256

        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(password, encPassword1));
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(password, encPassword2));
        assertThrows(IllegalArgumentException.class, () -> passwordEncoder.matches(password, encPassword3));
    }

    @Test
    void 암호변환기ID가_없는경우는_다음과같이() {
        String password = "password";
        String encPassword = "$2a$10$Ot44NE6k1kO5bfNHTP0m8ejdpGr8ooHGT90lOD2/LpGIzfiS3p6oq";                       // bcrypt

        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        assertThat(delegatingPasswordEncoder.matches(password, encPassword)).isTrue();
    }

    @Test
    void customDelegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        String idForEncode = "bcrypt";
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);

        String password = "password";
        String encPassword = passwordEncoder.encode(password);
        assertThat(passwordEncoder.matches(password, encPassword)).isTrue();

        String encPassword1 = "{pbkdf2}7a07c208fc2a407fb89cc3b6effb1b759da575a85f65dda9cd426f1ad14b56e6afaeeea6f9269569";
        String encPassword2 = "{bcrypt}$2a$10$Ot44NE6k1kO5bfNHTP0m8ejdpGr8ooHGT90lOD2/LpGIzfiS3p6oq";
        String encPassword3 = "{sha256}fcef9e3f82af42d9059e74a95c633fe99b7aba1c4bfb9ac1cae31dd1b67060da933776fee8baec8f";

        assertThat(passwordEncoder.matches(password, encPassword1)).isTrue();
        assertThat(passwordEncoder.matches(password, encPassword2)).isTrue();
        assertThat(passwordEncoder.matches(password, encPassword3)).isTrue();
    }
}
