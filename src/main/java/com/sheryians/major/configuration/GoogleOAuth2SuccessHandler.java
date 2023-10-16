//package com.sheryians.major.configuration;
//
//
//import com.sheryians.major.domain.Role;
//import com.sheryians.major.domain.User;
//import com.sheryians.major.repository.RoleRepository;
//import com.sheryians.major.repository.UserRepository;
//import org.apache.catalina.filters.ExpiresFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {
//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    UserRepository userRepository;
//
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//
//
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
//        String email = token.getPrincipal().getAttributes().get("email").toString();
//        if(userRepository.findByEmail(email).is){
//
//
//        }else {
//            User user = new User();
//            user.setFirstname(token.getPrincipal().getAttributes().get("given_name").toString());
//            user.setFirstname(token.getPrincipal().getAttributes().get("family_name").toString());
//            user.setEmail(email);
//            List<Role> roles = new ArrayList<>();
//            roles.add((Role) roleRepository.findById(2).get());
//            user.setRoles(roles);
//            userRepository.save(user);
//        }
//        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
//    }
//
//}
//
