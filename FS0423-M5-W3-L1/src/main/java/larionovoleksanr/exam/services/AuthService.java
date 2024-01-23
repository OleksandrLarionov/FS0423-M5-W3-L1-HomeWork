package larionovoleksanr.exam.services;

import larionovoleksanr.exam.Role;
import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.BadRequestException;
import larionovoleksanr.exam.exceptions.UnauthorizedException;
import larionovoleksanr.exam.payloads.DipendenteLogInDTO;
import larionovoleksanr.exam.payloads.NewEmployeeDTO;
import larionovoleksanr.exam.repositories.DipendenteDAO;
import larionovoleksanr.exam.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private DipendenteDAO dipendenteDAO;

    public String authenticateUser(DipendenteLogInDTO body) {
        // 1. Verifichiamo che l'email dell'utente sia nel db
        Dipendente employee = employeeService.findByEmail(body.email());

        // 2. In caso affermativo, verifichiamo se la password fornita corrisponde a quella trovata nel db
        if (bcrypt.matches(body.password(), employee.getPassword())) { // adesso con bcrypt verifico mette a paragone l'hash
            // 3. Se le credenziali sono OK --> Genere un token JWT e lo ritorno
            return jwtTools.createToken(employee);
        } else {
            // 4. Se le credenziali NON sono OK --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
    public Dipendente saveEmployee(NewEmployeeDTO body) {
        dipendenteDAO.findByEmail(body.email()).ifPresent(employee -> {
            throw new BadRequestException("IS ALREADY EXIST " + employee.getEmail());
        });
        Random rndm = new Random();
        Dipendente employee = new Dipendente();
        employee.setUsername(body.name() + body.surname() + rndm.nextInt(1, 100000));
        employee.setProfileImage("PROFILE IMAGE NOT UPLOADED YET");
        employee.setName(body.name());
        employee.setSurname(body.surname());
        employee.setEmail(body.email());
        //employee.setPassword(body.password());
        employee.setPassword(bcrypt.encode(body.password()));
        employee.setRole(Role.USER);
        return dipendenteDAO.save(employee);
    }

}
