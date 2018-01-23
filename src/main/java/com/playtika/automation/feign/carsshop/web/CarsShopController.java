package com.playtika.automation.feign.carsshop.web;

import com.playtika.automation.feign.carsshop.exception.CarOnSaleNotFoundException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileContentException;
import com.playtika.automation.feign.carsshop.exception.InvalidFileException;
import com.playtika.automation.feign.carsshop.exception.NoBestDealFoundException;
import com.playtika.automation.feign.carsshop.exception.NotAllRequiredParametersReceivedException;
import com.playtika.automation.feign.carsshop.model.CarReport;
import com.playtika.automation.feign.carsshop.model.Customer;
import com.playtika.automation.feign.carsshop.model.DealInfo;
import com.playtika.automation.feign.carsshop.service.CarShopService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by emuravjova on 12/19/2017.
 */
@RestController
@AllArgsConstructor
@Slf4j
@Api(description = "Add cars to Car store")
public class CarsShopController {

    @Autowired
    private CarShopService carService;

    @ApiOperation(value = "View a report of added cars")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Car report returns"),
            @ApiResponse(code = 400, message = "Invalid file or file content")})
    @PostMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CarReport> addCars(
            @ApiParam(name = "Path to file", required = true)
            @RequestBody String fileName) {
        return carService.addCars(fileName);
    }

    @PostMapping(value = "/deal", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create new deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deal with an ID was successfully created"),
            @ApiResponse(code = 404, message = "Such car is not on sale or no such car at all")})
    public DealInfo createDeal(
            @Valid @RequestBody Customer customer,
            @ApiParam(name = "price", required = true, defaultValue = "20000")
            @NotEmpty @RequestParam("price") int price,
            @ApiParam(name = "carId", required = true, defaultValue = "1")
            @NotEmpty @RequestParam("carId") Long id) {
        return carService.createDeal(id, price, customer);
    }

    @GetMapping(value = "/offer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find the best deal by offer id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The best deal is successfully found for offer"),
            @ApiResponse(code = 404, message = "No best deal found for open offer")})
    public DealInfo findTheBestDeal(@PathVariable("id") long id) {
        return carService.findTheBestDeal(id);
    }

    @PutMapping(value = "/acceptDeal/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Accept deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deal is successfully accepted"),
            @ApiResponse(code = 409, message = "Deal cannot be accepted for closed offer")})
    public ResponseEntity<Void> acceptDeal(@PathVariable("id") long id) {
        if (carService.acceptDeal(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/rejectDeal/{id}")
    @ApiOperation(value = "Reject deal")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The deal is successfully rejected")})
    public void rejectDeal(@PathVariable("id") long id) {
        carService.rejectDeal(id);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidFileException(InvalidFileException e) {
        log.error("Error while file handling", e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleInvalidFileContent(InvalidFileContentException e) {
        log.error("Error while file content handling", e);
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleCarOnSaleNotFoundException(CarOnSaleNotFoundException e) {
        log.warn("{}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNoBestDealFoundException(NoBestDealFoundException e) {
        log.warn("{}", e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleNoBestDealFoundException(NotAllRequiredParametersReceivedException e) {
        log.warn("{}", e.getMessage());
        return e.getMessage();
    }
}
