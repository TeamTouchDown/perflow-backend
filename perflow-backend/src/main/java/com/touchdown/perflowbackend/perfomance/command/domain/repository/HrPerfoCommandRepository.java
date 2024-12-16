package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HrPerfoCommandRepository extends JpaRepository<HrPerfo, Long> {

    @Query("SELECT h FROM HrPerfo h WHERE h.emp.empId = :empId AND FUNCTION('YEAR', h.createDatetime) = :currentYear")
    Optional<HrPerfo> findByEmpIdAndCurrentYear(@Param("empId") String empId, @Param("currentYear") int currentYear);

    Optional<HrPerfo> findByhrPerfoId(@Param("hrPerfoId") Long hrPerfoId);

}
