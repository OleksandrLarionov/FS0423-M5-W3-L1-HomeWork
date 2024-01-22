package larionovoleksanr.exam.controllers;

import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.BadRequestException;
import larionovoleksanr.exam.payloads.NewEmployeeDTO;
import larionovoleksanr.exam.payloads.NewEmployeeResponseDTO;
import larionovoleksanr.exam.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public Page<Dipendente> getAuthor(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String orderBy) {
        return employeeService.getEmployees(page, size, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeResponseDTO saveEmployee(@RequestBody @Validated NewEmployeeDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            Dipendente newAuthor = employeeService.saveEmployee(body);
            return new NewEmployeeResponseDTO(newAuthor.getId());
        }
    }

    @GetMapping("/{id}")
    public Dipendente findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PutMapping("/{id}/avatar")
    public Dipendente findByIdAndUpdate(@PathVariable Long id, @RequestBody Dipendente body) {
        return employeeService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable Long id) {
        employeeService.delete(id);
    }

    @PostMapping("/{employeeId}/upload")
    public String uploadProfileImage(@RequestParam("avatar") MultipartFile file, @PathVariable(required = true) Long employeeId) throws IOException {
        return employeeService.uploadPicture(file,employeeId);
    }

}
