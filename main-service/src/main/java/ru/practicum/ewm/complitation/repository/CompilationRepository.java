package ru.practicum.ewm.complitation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.complitation.model.Compilation;


public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
