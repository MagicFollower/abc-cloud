package com.abc.business.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserAccountVO
 *
 * @Description UserAccountVO
 * @Author -
 * @Date 2077/8/18 22:41
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountLoginVO {
    private String username;
    private String password;
}
