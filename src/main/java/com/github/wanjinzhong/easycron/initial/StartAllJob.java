package com.github.wanjinzhong.easycron.initial;
import java.util.List;

import com.github.wanjinzhong.easycron.constant.enums.JobStatus;
import com.github.wanjinzhong.easycron.dao.entity.Job;
import com.github.wanjinzhong.easycron.dao.repository.JobRepository;
import com.github.wanjinzhong.easycron.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class StartAllJob implements CommandLineRunner{

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Override
    public void run(String... args) throws Exception {
        List<Job> runningJobs = jobRepository.findByStatusCode(JobStatus.RUNNING.name());
        runningJobs.forEach(job -> jobService.runJob(job.getId()));
    }
}
