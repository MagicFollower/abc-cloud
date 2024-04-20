package com.abc.system.common.constants;

import lombok.Getter;

/**
 * RocketMQ消息发送延迟枚举类（18级）
 *
 * @Description RocketMQ消息发送延迟枚举类（18级）
 * @Author -
 * @Date 2077/9/9 11:29
 * @Version 1.0
 */
@Getter
public enum AbcMQDelayTimeEnum {
    DELAY_TIME_ZERO(0, "0S"),
    DELAY_TIME_ONE_SECOND(1, "1S"),
    DELAY_TIME_FIVE_SECOND(2, "5s"),
    DELAY_TIME_TEN_SECOND(3, "10s"),
    DELAY_TIME_THIRTY_SECOND(4, "30s"),
    DELAY_TIME_ONE_MINUTES(5, "1m"),
    DELAY_TIME_TWO_MINUTES(6, "2m"),
    DELAY_TIME_THEE_MINUTES(7, "3m"),
    DELAY_TIME_FOUR_MINUTES(8, "4m"),
    DELAY_TIME_FIVE_MINUTES(9, "5m"),
    DELAY_TIME_SIX_MINUTES(10, "6m"),
    DELAY_TIME_SEVEN_MINUTES(11, "7m"),
    DELAY_TIME_EIGHT_MINUTES(12, "8m"),
    DELAY_TIME_NINE_MINUTES(13, "9m"),
    DELAY_TIME_TEN_MINUTES(14, "10m"),
    DELAY_TIME_TWENTY_MINUTES(15, "20m"),
    DELAY_TIME_THIRTY_MINUTES(16, "30m"),
    DELAY_TIME_ONE_HOURS(17, "1h"),
    DELAY_TIME_TWO_HOURS(18, "2h");

    private final int delayTimeLevel;
    private final String message;

    AbcMQDelayTimeEnum(int delayTimeLevel, String message) {
        this.delayTimeLevel = delayTimeLevel;
        this.message = message;
    }

    public static String getDelayTimeNameByLevel(int delayTimeLevel) {
        for (AbcMQDelayTimeEnum value : values()) {
            if (value.getDelayTimeLevel() == delayTimeLevel) {
                return value.getMessage();
            }
        }

        return "";
    }

    public static AbcMQDelayTimeEnum getDelayTimeEnum(int delayTimeLevel) {
        for (AbcMQDelayTimeEnum value : values()) {
            if (value.getDelayTimeLevel() == delayTimeLevel) {
                return value;
            }
        }

        return null;
    }
}

