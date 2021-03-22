package org.urbanclimatemonitor.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
public class UcmBackendApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(UcmBackendApplication.class, args);
	}
}
