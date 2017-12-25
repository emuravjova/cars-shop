package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.InvalidFileContent;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by emuravjova on 12/19/2017.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/cars")
@Slf4j
@Api(description="Add cars to Car store")
public class CarsShopController {

    @Autowired
    private CarShopService carService;

    @ApiOperation(value = "View a report of added cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car report returns"),
            @ApiResponse(code = 400, message = "Invalid file or file content")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarReport> addCar(
            @ApiParam(name = "Path to file", required = true)
            @RequestBody String fileName){
        return carService.addCar(fileName);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidFileException(InvalidFileException e) {
        log.error("Error while file handling", e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidFileContent(InvalidFileContent e) {
        log.error("Error while file content handling", e);
        return e.getMessage();
    }
}
