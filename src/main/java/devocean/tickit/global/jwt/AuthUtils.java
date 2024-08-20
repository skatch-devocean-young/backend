package devocean.tickit.global.jwt;

import devocean.tickit.domain.User;
import devocean.tickit.global.constant.Role;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthUtils {

    // 현재 요청하는 사용자의 정보가 필요한 경우, AuthUtils.class의 함수를 호출하여 사용(Auth context로 모두 처리해두었음!!)

    private final UserRepository userRepository;

    public User getCurrentUser() {
        log.info("사용자 id -> {}", getCurrentUserId());

        return userRepository.findById(getCurrentUserId()).get();
    }

    public UUID getCurrentUserId() {
        Object principalObject = getPrincipal();

        // principal이 UserDetails 인스턴스인지 확인
        if (principalObject instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principalObject;

            // UserDetails 인스턴스에서 userId 획득
            return UUID.fromString(userDetails.getUsername()); // userId는 UserDetails의 username에 저장되어 있음
        }
        return null;
    }

    public Role getCurrentRole() {
        Object principalObject = getPrincipal();

        // principal이 UserDetails 인스턴스인지 확인
        if (principalObject instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principalObject;

            // UserDetails에서 권한 목록 가져오기
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            GrantedAuthority firstAuthority = authorities.iterator().next();
            String authorityString = firstAuthority.getAuthority();

            // UserDetails 인스턴스에서 Role String 획득
            return Role.valueOf(authorityString);
        }
        return null;
    }

    public Object getPrincipal() {
        // 사용자의 principal 가져오기
        return getAuthentication().getPrincipal();
    }

    public Authentication getAuthentication() {
        // SecurityContext에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }
}
