package org.hackstyle.init;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.hackstyle.servico.Servico;
import org.quartz.CronScheduleBuilder;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class Initialize implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            
            String cron = "0 0 22 1/1 * ? *";
            
            JobDetail job = newJob(Servico.class).withIdentity("job1", "group1").build();
            Trigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    //.withSchedule(simpleSchedule().withIntervalInMinutes(5)
                    //.repeatForever())
                    .build();
            
            scheduler.scheduleJob(job, trigger);
            
            System.out.println("Inicializando scheduler Quartz!");
        } catch (SchedulerException ex) {
            Logger.getLogger(Initialize.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {        
            
        /*
        try {        
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.shutdown();
            System.out.println("Desabilitando scheduler Quartz!");
        } catch (SchedulerException ex) {
            Logger.getLogger(Initialize.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
    
    
}
