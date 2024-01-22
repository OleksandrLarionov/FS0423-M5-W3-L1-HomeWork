package larionovoleksanr.exam.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewEmployeeDTO(
        @Email(message = "inserisci un indirizzo email valido e nel formato corretto es: john.doe@mail.com")
        String email,
        @NotEmpty(message = "il campo del nome è vuoto")
        @Size(min = 2, max = 20, message = "Il Nome deve contenere tra 2 e 20 caratteri")
        String name,
        @NotEmpty(message = "il campo del cognome è vuoto")
        @Size(min = 2, max = 20, message = "Il Cognome deve contenere tra 2 e 20 caratteri")
        String surname,
        @NotEmpty
        String password)
{
}
