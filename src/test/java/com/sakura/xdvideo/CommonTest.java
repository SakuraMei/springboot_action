package com.sakura.xdvideo;

import com.sakura.xdvideo.domain.User;
import com.sakura.xdvideo.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.Test;

public class CommonTest {

    @Test
    public void testGeneJwt() {

        User user = new User();
        user.setId(999);
        user.setHeadImg("www.xdclass.net");
        user.setName("xd");

        String token = JwtUtils.geneJsonWebToken(user);
        System.out.println(token);
    }

    @Test
    public void testCheck() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWt1cmEiLCJpZCI6OTk5LCJuYW1lIjoieGQiLCJpbWciOiJ3d3cueGRjbGFzcy5uZXQiLCJpYXQiOjE1NjI0MDQ2NzUsImV4cCI6MTU2MzAwOTQ3NX0.onEXLDfGKClHKU6Dc7nuRK5Xszmj92H_lqv_L_wWZk4";
        Claims claims = JwtUtils.checkJWT(token);
        if (claims != null) {
            String name = (String) claims.get("name");
            String img = (String) claims.get("img");
            int id = (Integer) claims.get("id");
            System.out.println(name);
            System.out.println(img);
            System.out.println(id);
        } else {
            System.out.println("非法token");
        }
    }
}
