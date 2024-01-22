package larionovoleksanr.exam.services;

import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.UnauthorizedException;
import larionovoleksanr.exam.payloads.DipendenteLogInDTO;
import larionovoleksanr.exam.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUser(DipendenteLogInDTO body) {
        // 1. Verifichiamo che l'email dell'utente sia nel db
        Dipendente employee = employeeService.findByEmail(body.email());

        // 2. In caso affermativo, verifichiamo se la password fornita corrisponde a quella trovata nel db
        if (body.password().equals(employee.getPassword())) {
            // 3. Se le credenziali sono OK --> Genere un token JWT e lo ritorno
            return jwtTools.createToken(employee);
        } else {
            // 4. Se le credenziali NON sono OK --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali non valide!");
        }
    }
}
