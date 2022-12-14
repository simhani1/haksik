package protocol;

import lombok.Getter;
import lombok.Setter;

import javax.swing.text.DefaultEditorKit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Protocol implements Serializable {
    // 프로토콜 타입에 대한 변수
    public static final int PT_ORDER = 1;
    public static final int PT_STOCK_RES = 2;
    public static final int PT_STOCK_REQ = 3;
    public static final int PT_LOGIN_REQ = 4;
    public static final int PT_LOGIN_RES = 5;
    public static final int PT_MAIN = 6;
    public static final int PT_EXIT_REQ = 7;
    public static final int PT_EXIT_RES = 8;
    public static final int PT_START_SERVER = 9;
    public static final int PT_ORDER_SUCCESS = 10;
    public static final int PT_ORDER_FAILED = 11;
    public static final int PT_SERVICE_REQ = 12;
    public static final int PT_SERVICE_RES = 13;
    public static final int PT_POINT_REQ = 14;
    public static final int PT_POINT_RES = 15;
    public static final int PT_POINT_LOOKUP_REQ = 16;
    public static final int PT_LOOKUP_RES = 17;
    public static final int PT_LOGIN_FAILED = 18;
    public static final int PT_HISTORY_REQ = 19;
    public static final int PT_HISTORY_RES = 20;

    // 프로토콜 종류의 길이
    public static final int LEN_PROTOCOL_TYPE = 1;

    // 초기 사이즈
    public static final int LEN_MAX_SIZE = 1000;


    // 로그인
    public static final int LEN_LOGIN_ID = 20;
    public static final int LEN_LOGIN_FAILED_MSG = 100;

    // 주문
    public static final int LEN_ORDER_AMOUNT = 20;
    public static final int LEN_ORDER_FOOD = 20;
    public static final int LEN_ORDER_PRICE = 20;
    public static final int LEN_CLIENT_POINT = 20;
    public static final int LEN_SUCCESS_MSG = 100;
    public static final int LEN_FAILED_MSG = 100;
    // 재고 현황
    public static final int LEN_STOCK_MENU = 50;
    public static final int LEN_STOCK_PRICE = 50;
    public static final int LEN_STOCK_AMOUNT = 50;

    // 서비스 요청
    public static final int LEN_SERVICE_TYPE = 5;
    public static final int LEN_SERVICE_MSG = 100;

    // 포인트 충전
    public static final int LEN_POINT_MSG = 100;

    protected int protocolType;
    private byte[] packet;   //프로토콜과 데이터의 저장공간이 되는 바이트배열

    // 기본 생성자
    public Protocol() {
        this(PT_START_SERVER);
    }

    // 생성자
    public Protocol(int protocolType) {
        this.protocolType = protocolType;
        getPacket(protocolType);
    }

    public byte[] getPacket(int protocolType) {
        if (packet == null) {
            switch (protocolType) {
                case PT_EXIT_REQ:
                case PT_LOGIN_RES:
                case PT_MAIN:
                case PT_STOCK_REQ:
                case PT_POINT_LOOKUP_REQ:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID];
                    break;
                case PT_POINT_RES:
                case PT_LOOKUP_RES:
                case PT_POINT_REQ:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_POINT_MSG];
                    break;
                case PT_EXIT_RES:
                case PT_LOGIN_REQ:
                    packet = new byte[LEN_PROTOCOL_TYPE];
                    break;
                case PT_SERVICE_REQ:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_SERVICE_TYPE];
                    break;
                case PT_SERVICE_RES:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_SERVICE_MSG];
                    break;
                case PT_STOCK_RES:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_STOCK_MENU + LEN_STOCK_PRICE + LEN_STOCK_AMOUNT];
                    break;
                case PT_ORDER:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD + LEN_ORDER_AMOUNT + LEN_ORDER_PRICE];
                    break;
                case PT_START_SERVER:
                    packet = new byte[LEN_MAX_SIZE];
                    break;
                case PT_ORDER_SUCCESS:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_SUCCESS_MSG];
                    break;
                case PT_LOGIN_FAILED:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_FAILED_MSG];
                    break;
                case PT_ORDER_FAILED:
                    packet = new byte[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_FAILED_MSG];
                    break;
            }
        }
        packet[0] = (byte) protocolType;   //packet 바이트배열의 첫번째 방에 프로토콜타입 상수를 셋팅해 놓는다.
        return packet;
    }

    public byte[] getPacket() {
        return packet;
    }

    //Default 생성자로 생성한 후 protocol.Protocol 클래스의 packet 데이터를 바꾸기 위한 메서드
    public void setPacket(int pt, byte[] buf) {
        packet = null;
        packet = getPacket(pt);
        protocolType = pt;
        System.arraycopy(buf, 0, packet, 0, packet.length);
    }

    // ID
    public String getId() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_ID).trim();
    }

    public void setId(String id) {
        System.arraycopy(id.getBytes(), 0, packet, LEN_PROTOCOL_TYPE, id.getBytes().length);
    }

    // MenuName
    public String getMenuName() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_STOCK_MENU).trim();
    }

    public void setMenuName(String menuName) {
        System.arraycopy(menuName.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, menuName.trim().getBytes().length);
    }

    // MenuPrice
    public String getMenuPrice() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_STOCK_MENU, LEN_STOCK_PRICE).trim();
    }

    public void setMenuPrice(String menuPrice) {
        System.arraycopy(menuPrice.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_STOCK_MENU, menuPrice.trim().getBytes().length);
    }

    // MenuAmount
    public String getMenuAmount() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_STOCK_MENU + LEN_STOCK_PRICE, LEN_STOCK_AMOUNT).trim();
    }

    public void setMenuAmount(String menuAmount) {
        System.arraycopy(menuAmount.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID +  + LEN_STOCK_MENU + LEN_STOCK_PRICE, menuAmount.trim().getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_STOCK_MENU + LEN_STOCK_PRICE + menuAmount.getBytes().length] = '\0';
    }

    // OrderFood
    public String getOrderFood() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_ORDER_FOOD).trim();
    }

    public void setOrderFood(String orderFood) {
        System.arraycopy(orderFood.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, orderFood.getBytes().length);
    }

    // OrderAmount
    public String getOrderAmount() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD, LEN_ORDER_FOOD).trim();
    }

    public void setOrderAmount(String orderAmount) {
        System.arraycopy(orderAmount.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD, orderAmount.getBytes().length);
    }

    // OrderPrice
    public String getOrderPrice() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD + LEN_ORDER_AMOUNT, LEN_ORDER_PRICE).trim();
    }

    public void setOrderPrice(String orderPrice) {
        System.arraycopy(orderPrice.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD + LEN_ORDER_AMOUNT, orderPrice.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + LEN_ORDER_FOOD + LEN_ORDER_AMOUNT + orderPrice.getBytes().length] = '\0';
    }

    // ClientPoint
    public String getClientPoint() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_CLIENT_POINT).trim();
    }

    public void setClientPoint(String clientPoint) {
        System.arraycopy(clientPoint.getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, clientPoint.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + clientPoint.getBytes().length] = '\0';
    }

    // ServiceType
    public String getServiceType() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_SERVICE_TYPE).trim();
    }

    public void setServiceType(String serviceType) {
        System.arraycopy(serviceType.trim().getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, serviceType.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID +  serviceType.getBytes().length] = '\0';
    }

    // ServiceMsg
    public String getServiceMsg() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_SERVICE_MSG).trim();
    }

    public void setServiceMsg(String serviceMsg) {
        System.arraycopy(serviceMsg.getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, serviceMsg.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + serviceMsg.getBytes().length] = '\0';
    }

    // PointMsg
    public String getPointMsg() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_POINT_MSG).trim();
    }

    public void setPointMsg(String pointMsg) {
        System.arraycopy(pointMsg.getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, pointMsg.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + pointMsg.getBytes().length] = '\0';
    }

    // SuccessMsg
    public String getSuccessMsg() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_SUCCESS_MSG).trim();
    }

    public void setSuccessMsg(String successMsg) {
        System.arraycopy(successMsg.getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, successMsg.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + successMsg.getBytes().length] = '\0';
    }

    // FailedMsg
    public String getFailedMsg() {
        return new String(packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, LEN_FAILED_MSG).trim();
    }

    public void setFailedMsg(String failedMsg) {
        System.arraycopy(failedMsg.getBytes(), 0, packet, LEN_PROTOCOL_TYPE + LEN_LOGIN_ID, failedMsg.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + LEN_LOGIN_ID + failedMsg.getBytes().length] = '\0';
    }

    // LoginFailedMsg
    public String getLoginFailedMsg() {
        return new String(packet, LEN_PROTOCOL_TYPE, LEN_LOGIN_FAILED_MSG).trim();
    }

    public void setLoginFailedMsg(String loginFailedMsg) {
        System.arraycopy(loginFailedMsg.getBytes(), 0, packet, LEN_PROTOCOL_TYPE, loginFailedMsg.getBytes().length);
        packet[LEN_PROTOCOL_TYPE + loginFailedMsg.getBytes().length] = '\0';
    }
}