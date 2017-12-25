package com.playtika.automation.feign.carsshop.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Created by emuravjova on 12/19/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarReport {
    CarSaleDetails carDetails;
    @ApiModelProperty(notes = "Oparation status", required = true, example = "ADDED")
    ReportStatus status;
}
