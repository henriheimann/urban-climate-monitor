#include "application.h"

#include <usart.h>
#include <stdio.h>
#include <i2c.h>
#include <spi.h>
#include <sht3x/sht3x.h>
#include <rfm95/rfm95.h>
#include <stdio2uart/stdio2uart.h>

_Noreturn void application_main()
{
	stdio2uart_init(&huart2);

	sht3x_handle_t sht3x_handle = {
			.i2c_handle = &hi2c1,
			.device_address = SHT3X_I2C_DEVICE_ADDRESS_ADDR_PIN_LOW
	};

	rfm95_handle_t rfm95_handle = {
			.spi_handle = &hspi1,
			.nss_port = RFM95_NSS_GPIO_Port,
			.nss_pin = RFM95_NSS_Pin,
			.nrst_port = RFM95_NRST_GPIO_Port,
			.nrst_pin = RFM95_NRST_Pin,
			.irq_port = RFM95_IRQ_GPIO_Port,
			.irq_pin = RFM95_IRQ_Pin
	};

	bool result = sht3x_init(&sht3x_handle);
	printf("Sensor SHT31 initialisation: %d\n\r", result);

	float temperature, humidity;
	sht3x_read_temperature_and_humidity(&sht3x_handle, &temperature, &humidity);
	printf("Initial temperature: %.2fC, humidity: %.2f%%RH\n\r", temperature, humidity);

	result = rfm95_init(&rfm95_handle);
	printf("Transceiver RFM95 initialisation: %d\n\r", result);

	while (1) {
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
		HAL_Delay(1000);
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);
		HAL_Delay(1000);
	}
}
