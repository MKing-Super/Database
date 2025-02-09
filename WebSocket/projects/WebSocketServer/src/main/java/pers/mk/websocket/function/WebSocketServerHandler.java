package pers.mk.websocket.function;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import pers.mk.websocket.util.UserContant;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;


/**
 * @Description: TODO
 * @Author: kun.ma
 * @Date: 2022/10/21 9:35
 */
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;


    private String userName = null;


    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        // 传统的HTTP接入
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // WebSocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,
                                   FullHttpRequest req) throws Exception {

        // 如果HTTP解码失败，返回HHTP异常
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }

        // 构造握手响应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8080/websocket", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory
                    .sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx,
                                      WebSocketFrame frame) {
        // 判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(),
                    (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是Ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format(
                    "%s frame types not supported", frame.getClass().getName()));
        }

        // 返回应答消息
        String request = ((TextWebSocketFrame) frame).text();
//        if (logger.isLoggable(Level.FINE)) {
//            logger.fine(String.format("%s received %s", ctx.channel(), request));
//        }
        JSONObject param = null;
        try {
            param = JSONObject.parseObject(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert param != null;
        log.info("json data -> {}", request);
        String formatDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        switch (param.get("type").toString()) {
            case "login":
                userName = param.get("message").toString();
                if (UserContant.onlineUserMap.containsKey(param.get("message").toString())){
                    ctx.channel().close();
                }else {
                    UserContant.onlineUserMap.put(param.get("message").toString(), ctx);
                    param.put("time",formatDate);
                    ctx.channel().write(new TextWebSocketFrame(param.toString()));
                }
                break;
            case "chat":
                for (Map.Entry<String, ChannelHandlerContext> entry : UserContant.onlineUserMap.entrySet()){
                    param.put("userName",userName);
                    param.put("time",formatDate);
                    entry.getValue().channel().write(new TextWebSocketFrame(param.toString()));
                    boolean active = entry.getValue().channel().isActive();
                    if (active){
                        System.out.println(entry.getKey());
                        entry.getValue().flush();
                    }else {
                        UserContant.onlineUserMap.remove(entry.getKey());
                    }
                }
                break;
            case "friends":
                for (Map.Entry<String, ChannelHandlerContext> entry : UserContant.onlineUserMap.entrySet()){
                    boolean active = entry.getValue().channel().isActive();
                    if (active){
                        entry.getValue().flush();
                    }else {
                        UserContant.onlineUserMap.remove(entry.getKey());
                    }
                }
                for (Map.Entry<String, ChannelHandlerContext> entry : UserContant.onlineUserMap.entrySet()){
                    Set<String> set = UserContant.onlineUserMap.keySet();
                    param.put("activeFriends",set);
                    entry.getValue().channel().write(new TextWebSocketFrame(param.toString()));
                }
                break;
            default:

        }

    }

    private static void sendHttpResponse(ChannelHandlerContext ctx,
                                         FullHttpRequest req, FullHttpResponse res) {
        // 返回应答给客户端
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
                    CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // 如果是非Keep-Alive，关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
