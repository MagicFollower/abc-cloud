package org.example.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * -
 *
 * @Description -
 * @Author -
 * @Date 2077/9/26 22:48
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserImportDTO {
    @ExcelProperty(index = 0)
    private BigDecimal id;

    @ExcelProperty(index = 1)
    private String name;

    @ExcelProperty(index = 2)
    private Date memo;
}
