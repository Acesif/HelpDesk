package com.grs.helpdeskmodule.service;

import com.grs.helpdeskmodule.base.BaseEntityRepository;
import com.grs.helpdeskmodule.base.BaseService;
import com.grs.helpdeskmodule.entity.Office;
import com.grs.helpdeskmodule.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;

    public Office getOffice(Long officeId) {
        return officeRepository.findById(officeId).orElse(null);
    }
    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }
}
