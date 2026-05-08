package com.forthems.ftnlabel.repository;


import com.forthems.ftnlabel.model.Ftn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FtnRepository extends JpaRepository<Ftn, String> {

    Ftn findByFtnNo(String word);
}
