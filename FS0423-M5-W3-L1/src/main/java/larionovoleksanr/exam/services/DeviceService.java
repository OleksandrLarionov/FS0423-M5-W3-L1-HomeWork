package larionovoleksanr.exam.services;

import larionovoleksanr.exam.entities.Dispositivo;
import larionovoleksanr.exam.exceptions.NotFoundException;
import larionovoleksanr.exam.repositories.DispositivoDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class DeviceService {
    @Autowired
    private DispositivoDAO dispositivoDAO;

    public Page<Dispositivo> getDevices(int page, int size, String orderBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return dispositivoDAO.findAll(pageable);
    }


    public Dispositivo saveDevice(Dispositivo body) {
        return dispositivoDAO.save(body);
    }

    public Dispositivo findById(Long id) {
        return dispositivoDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public void delete(Long id) {
        Dispositivo found = this.findById(id);
        dispositivoDAO.delete(found);

    }

    public Dispositivo findByIdAndUpdate(Long id, Dispositivo body) {
        Dispositivo found = this.findById(id);
        found.setId(id);
        found.setDeviceType(body.getDeviceType());
        found.setStateOfDevice(body.getStateOfDevice());
        if(body.getEmployee() != null){
            found.setEmployee(body.getEmployee());
        }
        return dispositivoDAO.save(found);
    }
}
