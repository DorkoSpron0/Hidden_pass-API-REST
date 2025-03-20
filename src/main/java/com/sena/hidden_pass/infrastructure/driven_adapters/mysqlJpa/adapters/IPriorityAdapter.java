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
        List<PriorityDBO> priorityDBOS = new ArrayList<>();
        priorityDBOS.add(new PriorityDBO(PriorityNames.LOW));
        priorityDBOS.add(new PriorityDBO(PriorityNames.MEDIUM));
        priorityDBOS.add(new PriorityDBO(PriorityNames.HIGH));
        priorityDBOS.add(new PriorityDBO(PriorityNames.CRITICAL));
        priorityRepository.saveAll(priorityDBOS);
    }
}
