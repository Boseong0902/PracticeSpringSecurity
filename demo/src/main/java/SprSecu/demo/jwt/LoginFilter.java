package SprSecu.demo.jwt;

import SprSecu.demo.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationmanager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationmanager, JWTUtil jwtUtil) {
        this.authenticationmanager = authenticationmanager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationmanager.authenticate(token);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        // username 뽑아내기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities(); // 권한이 여러개일수도 있으니까 collection으로 전달
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator(); // 권한이 여러개일수도 있으니까 순회
        GrantedAuthority auth = iterator.next(); // 권한 리스트에서 권한 객체를 뽑아낸다
        // authroities에서 권한 목록 뽑아내기
        String role = auth.getAuthority();
        // 권한 뽑아내기

        String token = jwtUtil.CreateToken(username, role, 60*60*10L);

        response.addHeader("Authorization", "Bearer " + token);
        System.out.println("role: " + role);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
