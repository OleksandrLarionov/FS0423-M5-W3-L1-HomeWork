package larionovoleksanr.exam.controllers;


import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.BadRequestException;
import larionovoleksanr.exam.payloads.DipendenteLogInDTO;
import larionovoleksanr.exam.payloads.DipendenteLoginResponseDTO;
import larionovoleksanr.exam.payloads.NewEmployeeDTO;
import larionovoleksanr.exam.payloads.NewEmployeeResponseDTO;
import larionovoleksanr.exam.services.AuthService;
import larionovoleksanr.exam.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    public DipendenteLoginResponseDTO login(@RequestBody DipendenteLogInDTO body) {
        String accessToken = authService.authenticateUser(body);
        return new DipendenteLoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEmployeeResponseDTO createEmployee(@RequestBody @Validated NewEmployeeDTO newEmployeePayload, BindingResult validation) {
        // Per completare la validazione devo in qualche maniera fare un controllo del tipo: se ci sono errori -> manda risposta con 400 Bad Request
        System.out.println(validation);
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException("Ci sono errori nel payload!"); // L'eccezione arriverà agli error handlers tra i quali c'è quello che manderà la risposta con status code 400
        } else {
            Dipendente newEmployee = employeeService.saveEmployee(newEmployeePayload);

            return new NewEmployeeResponseDTO(newEmployee.getId());
        }
    }
}
