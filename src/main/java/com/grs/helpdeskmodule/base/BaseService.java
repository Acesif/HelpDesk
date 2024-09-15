package com.grs.helpdeskmodule.base;

import lombok.RequiredArgsConstructor;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class BaseService<T extends BaseEntity> {

    private final BaseEntityRepository<T> baseRepository;

    public T findById(Long id) {
        return baseRepository.findById(id).orElse(null);
    }

    public T save(T entity) {
        entity.setCreateDate(new Date());
        entity.setFlag(true);
        return baseRepository.saveAndFlush(entity);
    }

    public T update(T entity) {
        entity.setUpdateDate(new Date());
        return baseRepository.save(entity);
    }

    public void delete(Long id) {
        T entity = baseRepository.findById(id).orElse(null);
        if (entity != null) {
            entity.setFlag(false);
            update(entity);
        }
    }

    public void hardDelete(Long id) {
        baseRepository.deleteById(id);
    }
}
