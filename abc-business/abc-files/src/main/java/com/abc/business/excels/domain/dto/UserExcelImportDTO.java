package com.abc.business.excels.domain.dto;

import com.abc.business.excels.domain.entity.User;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
public class UserExcelImportDTO {

    @ExcelProperty(index = 0)
    private String name;
    @ExcelProperty(index = 1)
    private String code;

    @ExcelProperty(index = 2)
    private Integer age;

    @ExcelProperty(index = 3)
    private LocalDateTime createTime;

    @ExcelProperty(index = 4)
    private boolean tag;

    @ExcelIgnore
    private String description;

    @Mapper
    public interface UserExcelImportDTOStruct {
        UserExcelImportDTOStruct copy = Mappers.getMapper(UserExcelImportDTOStruct.class);

        UserExcelImportDTO from(User user);

        List<UserExcelImportDTO> from(List<User> userList);

    }
}
