package SprSecu.demo.jwt;

import SprSecu.demo.dto.CustomUserDetails;
import SprSecu.demo.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request header 찾기
        String authorization = request.getHeader("Authorization");

        // request header로 받은 authorization검증
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // bearer제거
        String token = authorization.split(" ")[1];

        // 토큰 만료 시간 확인
        if(jwtUtil.isTokenExpired(token)){
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setRole(role);
        userEntity.setPassword("tempPassword");
        // 어차피 인증이 완료된 user의 UserEntity니까 비밀번호는 임시로 넣어줌 -> UserEntity의 필드는 채워야되니까
        // 인증은 어디서 했냐고? -> getUsernameFromToken, getRoleFromToken 내부적으로 구현

        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

        Authentication authToken = new UsernamePasswordAuthenticationToken(username, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
