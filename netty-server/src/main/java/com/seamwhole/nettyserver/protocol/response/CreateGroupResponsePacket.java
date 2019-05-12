package com.seamwhole.nettyserver.protocol.response;

import com.seamwhole.nettyserver.protocol.Packet;

import java.util.List;

import static com.seamwhole.nettyserver.protocol.command.Command.CREATE_GROUP_RESPONSE;


public class CreateGroupResponsePacket extends Packet {
    private boolean success;

    private String groupId;

    private List<String> userNameList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_RESPONSE;
    }
}