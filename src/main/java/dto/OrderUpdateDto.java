package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {
    private int id;
    private String status;
}