package com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.adapters;

import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.DBO.PriorityDBO;
import com.sena.hidden_pass.domain.models.PriorityNames;
import com.sena.hidden_pass.infrastructure.driven_adapters.mysqlJpa.IPriorityRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IPriorityAdapter implements ApplicationRunner {

    private final IPriorityRepository priorityRepository;

    public IPriorityAdapter(IPriorityRepository iPriorityRepository){
        this.priorityRepository = iPriorityRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception { // Insert when server starts
        List<PriorityDBO> priorityList = priorityRepository.findAll();

        if(priorityList.isEmpty()){
            List<PriorityDBO> priorityDBOS = new ArrayList<>();
            priorityDBOS.add(new PriorityDBO(PriorityNames.BAJA));
            priorityDBOS.add(new PriorityDBO(PriorityNames.MEDIA));
            priorityDBOS.add(new PriorityDBO(PriorityNames.ALTA));
            priorityDBOS.add(new PriorityDBO(PriorityNames.CRITICA));
            priorityRepository.saveAll(priorityDBOS);
        }
    }
}
