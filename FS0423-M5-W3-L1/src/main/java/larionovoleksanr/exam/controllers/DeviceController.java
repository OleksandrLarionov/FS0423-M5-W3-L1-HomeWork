package larionovoleksanr.exam.controllers;

import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.entities.Dispositivo;
import larionovoleksanr.exam.exceptions.BadRequestException;
import larionovoleksanr.exam.payloads.NewDeviceDTO;
import larionovoleksanr.exam.payloads.NewDeviceResponseDTO;
import larionovoleksanr.exam.services.DeviceService;
import larionovoleksanr.exam.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import  larionovoleksanr.exam.beansConfiguration.MailGunSender;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    MailGunSender mailGunSender;

    @GetMapping
    public Page<Dispositivo> getDevices(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "0") int size,
                                        @RequestParam(defaultValue = "id") String orderBy) {
        return deviceService.getDevices(page, size, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dispositivo saveDevice(@RequestBody @Validated NewDeviceDTO payload, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }

        Dispositivo device = new Dispositivo();
        device.setDeviceType(payload.deviceType());
        device.setStateOfDevice("DISPONIBILE");

        Long employeeId = payload.idEmployee();
        if (employeeId != null) {
            Dipendente employee = employeeService.findById(employeeId);
            device.setEmployee(employee);
        }

        return deviceService.saveDevice(device);
    }


    @GetMapping("/{id}")
    public Dispositivo findById(@PathVariable Long id) {
        return deviceService.findById(id);
    }

    @PutMapping("/{id}")
    public NewDeviceResponseDTO findByIdAndUpdate(@PathVariable Long id,
                                                  @RequestBody @Validated NewDeviceDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        Dispositivo deviceUptede = new Dispositivo();
        deviceUptede.setDeviceType(body.deviceType());
//        non sono sicuro se aggiungere anche lo stateOfDevice nel paylod e anche qui.

        if(body.idEmployee() != null){
            deviceUptede.setEmployee(employeeService.findById(body.idEmployee()));
            mailGunSender.sendRegistrationMail(deviceUptede.getEmployee().getEmail(), deviceUptede);
        }
        deviceService.findByIdAndUpdate(id, deviceUptede);
        return new NewDeviceResponseDTO("Il dispositivo è stato aggiornato con l'id: ", deviceUptede.getId());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable Long id) {
        deviceService.delete(id);
    }
}
