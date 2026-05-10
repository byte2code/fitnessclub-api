package com.CN.fitnessClub.service;

import com.CN.fitnessClub.dto.JwtRequest;
import com.CN.fitnessClub.dto.JwtResponse;
import com.CN.fitnessClub.jwt.JwtAuthenticationHelper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootTest
@DisplayName("Testing AuthService")
public class AuthServiceTests {
    @InjectMocks
    AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtAuthenticationHelper jwtAuthenticationHelper;
    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing for Valid Login")
    public void shouldTestForValidLogin() {
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        String token = "testToken";
        Mockito.when(userDetailsService.loadUserByUsername(jwtRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(jwtAuthenticationHelper.generateToken(userDetails)).thenReturn(token);
        JwtResponse jwtResponse = authService.login(jwtRequest);
        Assertions.assertNotNull(jwtResponse);
        Assertions.assertEquals(token, jwtResponse.getJwtToken());
    }

    @Test
    @DisplayName("Testing for Invalid Login")
    public void shouldTestForInvalidLogin() {
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        String token = "testToken";
        Mockito.when(userDetailsService.loadUserByUsername(jwtRequest.getUsername())).thenReturn(userDetails);
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new BadCredentialsException("Invalid Username or Password"));
        Assertions.assertThrows(BadCredentialsException.class, () -> authService.login(jwtRequest));
    }
}
