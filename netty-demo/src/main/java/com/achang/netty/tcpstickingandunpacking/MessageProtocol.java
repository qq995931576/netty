package com.achang.netty.tcpstickingandunpacking;

/******
 @author 阿昌
 @create 2021-12-12 23:55
 *******
 *  自定义协议包
 */
public class MessageProtocol {
    private int len; //关键，通过这个来控制长度
    private byte[] content;
    public int getLen() {
        return len;
    }
    public void setLen(int len) {
        this.len = len;
    }
    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }
}
