import org.quartz.CronScheduleBuilder;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {
	public static void main(String[] args){
		
		JobDetail job = JobBuilder.newJob(Wolf.class).build();
		
		//Trigger t = TriggerBuilder.newTrigger().withIdentity("MinTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *")).build();
		Trigger t = TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * 1/1 * ? *")).build();
		Scheduler s;
		try {
			s = StdSchedulerFactory.getDefaultScheduler();
			try {
				s.start();
				s.scheduleJob(job, t);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
	}
}
