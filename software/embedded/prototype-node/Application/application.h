#pragma once

#include <sht3x/sht3x.h>
#include <rfm95/rfm95.h>
#include <eeprom/eeprom.h>

extern sht3x_handle_t sht3x_handle;
extern rfm95_handle_t rfm95_handle;
extern eeprom_handle_t eeprom_handle;

/**
 * Application main called by the STM32Cube init code.
 */
void application_main();