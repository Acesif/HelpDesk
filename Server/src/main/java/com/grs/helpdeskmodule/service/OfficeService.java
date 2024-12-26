package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Office;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService extends BaseService<Office> {

    private final OfficeRepository officeRepository;

    public OfficeService(BaseEntityRepository<Office> baseRepository, OfficeRepository officeRepository) {
        super(baseRepository);
        this.officeRepository = officeRepository;
    }

    public Office getOffice(Long officeId) {
        return officeRepository.findById(officeId).orElse(null);
    }
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }
}
