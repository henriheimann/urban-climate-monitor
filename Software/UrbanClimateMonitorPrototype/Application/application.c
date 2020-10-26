#include "application.h"

#include <usart.h>
#include <stdio.h>
#include <i2c.h>
#include <sht3x/sht3x.h>
#include <stdio2uart/stdio2uart.h>

_Noreturn void application_main()
{
	stdio2uart_init(&huart2);

	sht3x_handle_t handle = {
			.i2c_handle = &hi2c1,
			.device_address = SHT3X_I2C_DEVICE_ADDRESS_ADDR_PIN_LOW
	};

	bool result = sht3x_init(&handle);
	printf("Sensor SHT31 initialisation: %d\n\r", result);

	float temperature;
	float humidity;

	sht3x_read_temperature_and_humidity(&handle, &temperature, &humidity);
	printf("Initial Temperature: %.2fC, Humidity: %.2f%%RH\n\r", temperature, humidity);

	uint32_t start_time = HAL_GetTick();
	uint32_t heat_end_time = start_time + 10000;
	bool heater_off = false;

	printf("Heater ON\n\r");
	sht3x_set_header_enable(&handle, true);

	while (1) {

		if (HAL_GetTick() >= heat_end_time && !heater_off) {
			printf("Heater OFF\n\r");
			sht3x_set_header_enable(&handle, false);
			heater_off = true;
		}

		sht3x_read_temperature_and_humidity(&handle, &temperature, &humidity);
		printf("Temperature: %.2fC, Humidity: %.2f%%RH\n\r", temperature, humidity);

		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
		HAL_Delay(1000);
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);
		HAL_Delay(1000);
	}
}
