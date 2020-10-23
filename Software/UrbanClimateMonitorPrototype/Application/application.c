#include <stdio2uart/stdio2uart.h>
#include "application.h"

#include <usart.h>
#include <stdio.h>

_Noreturn void application_main()
{
	stdio2uart_init(&huart2);

	while (1) {
		printf("Hello World!\n\r");
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_SET);
		HAL_Delay(1000);
		HAL_GPIO_WritePin(LD3_GPIO_Port, LD3_Pin, GPIO_PIN_RESET);
		HAL_Delay(1000);
	}
}