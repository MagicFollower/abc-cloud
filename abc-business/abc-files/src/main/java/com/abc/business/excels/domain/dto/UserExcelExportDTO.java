package com.abc.business.excels.domain.dto;

import com.abc.business.excels.domain.entity.User;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author trivis
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserExcelExportDTO {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;
    @ExcelProperty(value = "编号", index = 1)
    private String code;

    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;

    @ExcelProperty(value = "创建时间", index = 3)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    private LocalDateTime createTime;

    @ExcelIgnore
    private String description;


    @Mapper
    public interface UserExcelExportDTOStruct {
        UserExcelExportDTOStruct copy = Mappers.getMapper(UserExcelExportDTOStruct.class);
        UserExcelExportDTO from(User user);
        List<UserExcelExportDTO> from(List<User> userList);

    }
}
