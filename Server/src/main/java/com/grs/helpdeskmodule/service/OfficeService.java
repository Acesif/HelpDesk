package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.entity.Office;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public Office findById(Long id) {
        return officeRepository.findById(id).orElse(null);
    }

    public Office save(Office entity) {
        entity.setCreateDate(new Date());
        entity.setFlag(true);
        return officeRepository.saveAndFlush(entity);
    }

    public Office update(Office entity) {
        entity.setUpdateDate(new Date());
        return officeRepository.save(entity);
    }

    public void delete(Long id) {
        Office entity = officeRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setFlag(false);
            update(entity);
        }
    }

    public void hardDelete(Long id) {
        officeRepository.deleteById(id);
    }

    public Office getOffice(Long officeId) {
        return officeRepository.findById(officeId).orElse(null);
    }
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }
}
