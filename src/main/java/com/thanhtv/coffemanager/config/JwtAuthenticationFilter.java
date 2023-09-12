package com.thanhtv.coffemanager.config;

import com.thanhtv.coffemanager.service.jwtService.JwtService;
import com.thanhtv.coffemanager.service.userService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
    @Autowired
    private JwtService jwtService;
    @Autowired
    private IUserService iUserService;

    private String getBearTokenRequest(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null){
            if(authHeader.startsWith("Bearer")){
                return authHeader.replace("Bearee","");
            }
            return authHeader;
        }
        return null;
    }

    private String getCookieValue(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String tokenName = "JWT";
        if(cookies != null){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(tokenName)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, javax.servlet.FilterChain filterChain)
            throws ServletException, IOException {
        try{
            String bearToken = getBearTokenRequest(request);
            String authorizationToken =getCookieValue(request);
            setAuthentication(request,bearToken);
            setAuthentication(request,authorizationToken);
        }catch (Exception e){
            logger.error("Can NOT set user authentication -> Message: {0}", e);
        }
        filterChain.doFilter(request,response);
    }

    private void setAuthentication(HttpServletRequest request, String authorizationToken) {
        if(authorizationToken != null && jwtService.validateJwtToken(authorizationToken)){
            String userName = jwtService.getUserNameFromJwtToken(authorizationToken);
            UserDetails userDetails = iUserService.loadUserByUsername(userName);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
