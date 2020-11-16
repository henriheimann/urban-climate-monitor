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
		.irq_port = RFM95_DIO0_GPIO_Port,
		.irq_pin = RFM95_DIO0_Pin,
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
	if (!eeprom_write_bytes(&eeprom_handle, 0x00, buffer, sizeof(buffer))) {
		printf("EEPROM write error\n\r");
	}
}

static uint32_t adc_get_sample(int num_samples, uint32_t delay_between)
{
	uint32_t adc_value = 0;
	for (int i = 0; i < num_samples; i++) {
		HAL_ADC_Start(&hadc1);
		HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
		adc_value += HAL_ADC_GetValue(&hadc1);
		HAL_ADC_Stop(&hadc1);
		HAL_Delay(delay_between);
	}
	return adc_value / num_samples;
}

static float adc_sample_to_photo_diode_current(uint32_t adc_sample, uint32_t amplification)
{
	float voltage = ((float)adc_sample * 3.3f) / 4096.0f  - 97e-3f;
	return voltage / (float)(amplification) * 10e9f;
}

static float read_photo_diode_current()
{
	HAL_ADCEx_Calibration_Start(&hadc1, ADC_SINGLE_ENDED);

	HAL_GPIO_WritePin(PHOTO_ENABLE_GPIO_Port, PHOTO_ENABLE_Pin, GPIO_PIN_SET);

	HAL_GPIO_WritePin(PHOTO_SWITCH_GPIO_Port, PHOTO_SWITCH_Pin, GPIO_PIN_RESET);
	HAL_Delay(10);
	uint32_t adc_value_low_amp = adc_get_sample(10, 1);
	uint32_t adc_value_high_amp = 0;
	float photo_diode_current = adc_sample_to_photo_diode_current(adc_value_low_amp, 33000);

	if (adc_value_low_amp < 252) {
		HAL_GPIO_WritePin(PHOTO_SWITCH_GPIO_Port, PHOTO_SWITCH_Pin, GPIO_PIN_SET);
		HAL_Delay(10);
		adc_value_high_amp = adc_get_sample(20, 1);
		photo_diode_current = adc_sample_to_photo_diode_current(adc_value_high_amp, 1000000);
	}

	if (photo_diode_current < 0) {
		photo_diode_current = 0;
	}

	HAL_GPIO_WritePin(PHOTO_ENABLE_GPIO_Port, PHOTO_ENABLE_Pin, GPIO_PIN_RESET);
	return photo_diode_current;
}

typedef struct {
	int16_t temperature;
	uint16_t humidity;
	int16_t ir_temperature;
	uint32_t brightness_current;
	uint8_t battery_voltage;
} __packed data_packet_t;

void application_main()
{
	HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
	HAL_Delay(1000);
	HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);

	float temperature, humidity;
	float photo_diode_current;

	stdio2uart_init(&huart2);

	sht3x_init(&sht3x_handle);
	sht3x_read_temperature_and_humidity(&sht3x_handle, &temperature, &humidity);

	photo_diode_current = read_photo_diode_current();

	rfm95_init(&rfm95_handle);
	data_packet_t data_packet = {0};
	data_packet.temperature = (int16_t)(temperature * 100);
	data_packet.humidity = (uint16_t)(humidity * 100);
	data_packet.brightness_current = (uint32_t)photo_diode_current;
	rfm95_send_data(&rfm95_handle, (uint8_t*)(&data_packet), sizeof(data_packet));
}
