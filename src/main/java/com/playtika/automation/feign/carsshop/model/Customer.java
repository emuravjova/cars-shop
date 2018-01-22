package com.playtika.automation.feign.carsshop.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Customer {
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Customer name", required = true, example = "Den")
    String name;
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Customer contacts", required = true, example = "0967894512")
    String contacts;
}
