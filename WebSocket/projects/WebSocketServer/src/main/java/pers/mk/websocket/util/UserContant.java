package pers.mk.websocket.util;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 用户信息存储
 * @Author: kun.ma
 * @Date: 2022/10/21 14:37
 */
public class UserContant {

    public static Map<String, ChannelHandlerContext> onlineUserMap = new ConcurrentHashMap<String, ChannelHandlerContext>();

}
