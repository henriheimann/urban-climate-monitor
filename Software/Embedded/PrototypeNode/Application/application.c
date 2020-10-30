#include "application.h"

#include <usart.h>
#include <stdio.h>
#include <i2c.h>
#include <spi.h>
#include <adc.h>
#include <stdio2uart/stdio2uart.h>

static bool reload_frame_counter(uint16_t *tx_counter, uint16_t *rx_counter);
static void save_frame_counter(uint16_t tx_counter, uint16_t rx_counter);

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
		.irq_pin = RFM95_IRQ_Pin,
		.dio5_port = RFM95_DIO5_GPIO_Port,
		.dio5_pin = RFM95_DIO5_Pin,
		.device_address = {0x26, 0x01, 0x16, 0xCE},
		.application_session_key = {0xB3, 0xFC, 0x86, 0xAD, 0xB7, 0xD3, 0x72, 0x1A, 0x61, 0x74, 0x4F, 0x4B, 0xFF, 0xE7, 0xE7, 0x33},
		.network_session_key = {0x8E, 0xCE, 0xE3, 0x9E, 0x21, 0x84, 0x33, 0x69, 0xA8, 0x3A, 0xA6, 0x12, 0x3D, 0xB1, 0x73, 0x48},
		.reload_frame_counter = reload_frame_counter,
		.save_frame_counter = save_frame_counter
};

eeprom_handle_t eeprom_handle = {
		.i2c_handle = &hi2c1,
		.device_address = EEPROM_24LC32A_ADDRESS,
		.max_address = EEPROM_24LC32A_MAX_ADDRESS,
		.page_size = EEPROM_24LC32A_PAGE_SIZE
};

static bool reload_frame_counter(uint16_t *tx_counter, uint16_t *rx_counter)
{
	uint8_t buffer[6];

	if (!eeprom_read_bytes(&eeprom_handle, 0x00, buffer, sizeof(buffer))) {
		return false;
	}

	if (buffer[0] == 0x1A && buffer[1] == 0xA1) {
		*tx_counter = (uint16_t)((uint16_t)buffer[2] << 8u) | (uint16_t)buffer[3];
		*rx_counter = (uint16_t)((uint16_t)buffer[4] << 8u) | (uint16_t)buffer[5];
	} else {
		*tx_counter = 0;
		*rx_counter = 0;
	}
	return true;
}

static void save_frame_counter(uint16_t tx_counter, uint16_t rx_counter)
{
	uint8_t buffer[6] = {
			0x1A, 0xA1, (uint8_t)(tx_counter >> 8u) & 0xffu, tx_counter & 0xffu, (uint8_t)(rx_counter >> 8u) & 0xffu, rx_counter & 0xffu
	};
	eeprom_write_bytes(&eeprom_handle, 0x00, buffer, sizeof(buffer));
}

void test_photo_sense()
{
	HAL_GPIO_WritePin(PHOTO_SWITCH_GPIO_Port, PHOTO_SWITCH_Pin, GPIO_PIN_RESET);
	HAL_Delay(10);

	uint32_t adc_value_1 = 0;
	for (int i = 0; i < 50; i++) {
		HAL_ADC_Start(&hadc1);
		HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
		adc_value_1 += HAL_ADC_GetValue(&hadc1);
		HAL_ADC_Stop(&hadc1);
		HAL_Delay(1);
	}
	adc_value_1 /= 50;

	HAL_GPIO_WritePin(PHOTO_SWITCH_GPIO_Port, PHOTO_SWITCH_Pin, GPIO_PIN_SET);
	HAL_Delay(10);

	uint32_t adc_value_2 = 0;
	for (int i = 0; i < 50; i++) {
		HAL_ADC_Start(&hadc1);
		HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
		adc_value_2 += HAL_ADC_GetValue(&hadc1);
		HAL_ADC_Stop(&hadc1);
		HAL_Delay(1);
	}
	adc_value_2 /= 50;

	printf("Photo ADC readings: %lu %lu\n\r", adc_value_1, adc_value_2);
}

_Noreturn void application_main()
{
	stdio2uart_init(&huart2);

	bool result = sht3x_init(&sht3x_handle);
	printf("Sensor SHT31 initialisation: %d\n\r", result);

	float temperature, humidity;
	sht3x_read_temperature_and_humidity(&sht3x_handle, &temperature, &humidity);
	printf("Initial temperature: %.2fC, humidity: %.2f%%RH\n\r", temperature, humidity);

	result = rfm95_init(&rfm95_handle);
	printf("Transceiver RFM95 initialisation: %d\n\r", result);

	char *dataToSend = "Hello World!";

	result = rfm95_send_data(&rfm95_handle, (uint8_t*)dataToSend, 12);
	printf("Send success: %d\n\r", result);

	while (1) {
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
		HAL_Delay(1000);
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);
		HAL_Delay(1000);
		test_photo_sense();
	}
}
