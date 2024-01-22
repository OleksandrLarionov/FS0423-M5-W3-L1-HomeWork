package larionovoleksanr.exam.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import larionovoleksanr.exam.entities.Dipendente;
import larionovoleksanr.exam.exceptions.BadRequestException;
import larionovoleksanr.exam.exceptions.NotFoundException;
import larionovoleksanr.exam.payloads.NewEmployeeDTO;
import larionovoleksanr.exam.repositories.DipendenteDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

@Service
@Slf4j
public class EmployeeService {
    @Autowired
    DipendenteDAO dipendenteDAO;
    @Autowired
    private Cloudinary cloudinaryUploader;

    public Page<Dipendente> getEmployees(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return dipendenteDAO.findAll(pageable);
    }


    public Dipendente saveEmployee(NewEmployeeDTO body) {
        dipendenteDAO.findByEmail(body.email()).ifPresent(employee -> {
            throw new BadRequestException("IS ALREADY EXIST" + employee.getEmail());
        });
        Random rndm = new Random();
        Dipendente employee = new Dipendente();
        employee.setUsername(body.name() + body.surname() + rndm.nextInt(1, 100000));
        employee.setProfileImage("PROFILE IMAGE NOT UPLOADED YET");
        employee.setName(body.name());
        employee.setSurname(body.surname());
        employee.setEmail(body.email());
        employee.setPassword(body.password());
        return dipendenteDAO.save(employee);
    }

    public Dipendente findById(Long id) {
        return dipendenteDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void delete(Long id) {
        Dipendente found = this.findById(id);
        dipendenteDAO.delete(found);

    }

    public Dipendente findByIdAndUpdate(Long id, Dipendente body) {
        Dipendente found = this.findById(id);
        found.setId(id);
        found.setName(body.getName());
        found.setSurname(body.getSurname());
        found.setEmail(body.getEmail());
        return dipendenteDAO.save(found);
    }
    public Dipendente findByEmail(String email) throws NotFoundException {
        return dipendenteDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovata!"));
    }

    public String uploadPicture(MultipartFile file, Long authorId) throws IOException {
        String url = (String) cloudinaryUploader.uploader()
                .upload(file.getBytes(), ObjectUtils.emptyMap())
                .get("url");
        Dipendente found = this.findById(authorId);
        found.setProfileImage(url);
        dipendenteDAO.save(found);
        return "L'immagine è stata Aggiornata";
    }
}
