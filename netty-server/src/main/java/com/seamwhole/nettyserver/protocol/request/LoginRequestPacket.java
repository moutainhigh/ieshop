package com.seamwhole.nettyserver.protocol.request;

import com.seamwhole.nettyserver.protocol.Packet;

import static com.seamwhole.nettyserver.protocol.command.Command.LOGIN_REQUEST;

public class LoginRequestPacket extends Packet {
    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Byte getCommand() {

        return LOGIN_REQUEST;
    }
}
