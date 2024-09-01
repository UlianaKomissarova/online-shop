package com.example.aston.user.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOutput {
    private int id;
    private String name;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private boolean isVendor;
}