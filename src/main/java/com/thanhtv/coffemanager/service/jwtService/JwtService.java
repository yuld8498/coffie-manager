package com.thanhtv.coffemanager.service.jwtService;

import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "GoiTenToiNheBanThanHoiCoToiLuonCungChiaSotDeRoiTaLaiCoThemNiemTin";
    public static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60*24;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class.getName());

    public String generateTokenLogin(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.ES256, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try{
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJwt(authToken);
            return true;
        }catch (SignatureException signatureException){
            logger.error("Invalid JWT signature -> Message: {0} ", signatureException);
        }catch (MalformedJwtException malformedJwtException){
            logger.error("Malformed Jwt Exception -> Message: {0} ", malformedJwtException);
        }catch (ExpiredJwtException expiredJwtException){
            logger.error("Expired Jwt Exception -> Message: {0} ", expiredJwtException);
        }catch (UnsupportedJwtException unsupportedJwtException){
            logger.error("unsupported Jwt Exception -> Message: {0} ", unsupportedJwtException);
        }catch (IllegalAccessError illegalAccessError){
            logger.error("illegal Access Error -> Message: {0} ", illegalAccessError);
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJwt(token)
                .getBody().getSubject();
    }
}
