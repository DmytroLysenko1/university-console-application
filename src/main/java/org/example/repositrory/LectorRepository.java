package org.example.repositrory;

import org.example.entity.Lector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LectorRepository extends JpaRepository<Lector, Long> {
    List<Lector> findByNameContaining(String name);

    @Query("SELECT AVG(l.salary) FROM Lector l JOIN l.department d WHERE d.departmentName = :departmentName")
    BigDecimal fetchAverageSalaryByDepartmentName(@Param("departmentName") String departmentName);
}
