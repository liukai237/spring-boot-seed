package com.iakuil.bf.ws;

import lombok.ToString;

import javax.security.auth.Subject;
import java.security.Principal;

@ToString(of = "name")
public class WsUser implements Principal {

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public WsUser(String name) {
        this.name = name;
    }
}
