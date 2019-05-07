package com.seamwhole.nettyserver.protocol.response;

import com.seamwhole.nettyserver.protocol.Packet;

import static com.seamwhole.nettyserver.protocol.command.Command.JOIN_GROUP_RESPONSE;

public class JoinGroupResponsePacket extends Packet {
    private String groupId;

    private boolean success;

    private String reason;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public Byte getCommand() {

        return JOIN_GROUP_RESPONSE;
    }
}
