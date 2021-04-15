package org.urbanclimatemonitor.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class UcmBackendApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(UcmBackendApplication.class, args);
	}
}
