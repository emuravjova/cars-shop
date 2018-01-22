package com.playtika.automation.feign.carsshop.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by emuravjova on 1/22/2018.
 */
@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AddNewCarResponse {
    long id;
}
