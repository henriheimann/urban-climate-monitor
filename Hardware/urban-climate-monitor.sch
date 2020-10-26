EESchema Schematic File Version 4
EELAYER 30 0
EELAYER END
$Descr A3 16535 11693
encoding utf-8
Sheet 1 1
Title ""
Date ""
Rev ""
Comp ""
Comment1 ""
Comment2 ""
Comment3 ""
Comment4 ""
$EndDescr
$Comp
L Device:R R1
U 1 1 5F915A91
P 13200 2200
F 0 "R1" V 13407 2200 50  0000 C CNN
F 1 "1M" V 13316 2200 50  0000 C CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 13130 2200 50  0001 C CNN
F 3 "~" H 13200 2200 50  0001 C CNN
	1    13200 2200
	0    -1   -1   0   
$EndComp
$Comp
L Device:C C4
U 1 1 5F915A97
P 12450 2150
F 0 "C4" H 12565 2196 50  0000 L CNN
F 1 "4.7uF" H 12565 2105 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 12488 2000 50  0001 C CNN
F 3 "~" H 12450 2150 50  0001 C CNN
	1    12450 2150
	1    0    0    -1  
$EndComp
$Comp
L Device:C C1
U 1 1 5F915A9D
P 15150 1950
F 0 "C1" H 15265 1996 50  0000 L CNN
F 1 "4.7uF" H 15265 1905 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 15188 1800 50  0001 C CNN
F 3 "~" H 15150 1950 50  0001 C CNN
	1    15150 1950
	1    0    0    -1  
$EndComp
$Comp
L Device:L L1
U 1 1 5F915AA3
P 13200 1600
F 0 "L1" V 13019 1600 50  0000 C CNN
F 1 "4.7uH" V 13110 1600 50  0000 C CNN
F 2 "my-footprints-library:L_Bourns-SRN4018_With3DModel" H 13200 1600 50  0001 C CNN
F 3 "~" H 13200 1600 50  0001 C CNN
	1    13200 1600
	0    1    1    0   
$EndComp
Wire Wire Line
	13450 2200 13350 2200
Wire Wire Line
	13450 1900 12950 1900
Wire Wire Line
	13050 2200 12950 2200
Wire Wire Line
	12950 2200 12950 1900
Connection ~ 12950 1900
Wire Wire Line
	13350 1600 13850 1600
Wire Wire Line
	13850 1600 13850 1750
Wire Wire Line
	13050 1600 12950 1600
Wire Wire Line
	12950 1600 12950 1900
Wire Wire Line
	12450 2000 12450 1900
Wire Wire Line
	12450 1900 12950 1900
Connection ~ 12450 1900
Wire Wire Line
	14250 1900 14400 1900
Wire Wire Line
	14400 1900 14400 1700
Wire Wire Line
	15150 1700 15150 1800
Wire Wire Line
	15150 2100 15150 2700
Wire Wire Line
	12450 2300 12450 2700
$Comp
L power:GND #PWR05
U 1 1 5F915AC3
P 12450 2700
F 0 "#PWR05" H 12450 2450 50  0001 C CNN
F 1 "GND" H 12455 2527 50  0000 C CNN
F 2 "" H 12450 2700 50  0001 C CNN
F 3 "" H 12450 2700 50  0001 C CNN
	1    12450 2700
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR06
U 1 1 5F915AC9
P 13850 2700
F 0 "#PWR06" H 13850 2450 50  0001 C CNN
F 1 "GND" H 13855 2527 50  0000 C CNN
F 2 "" H 13850 2700 50  0001 C CNN
F 3 "" H 13850 2700 50  0001 C CNN
	1    13850 2700
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR07
U 1 1 5F915AD5
P 15150 2700
F 0 "#PWR07" H 15150 2450 50  0001 C CNN
F 1 "GND" H 15155 2527 50  0000 C CNN
F 2 "" H 15150 2700 50  0001 C CNN
F 3 "" H 15150 2700 50  0001 C CNN
	1    15150 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	13850 2700 13850 2350
Wire Wire Line
	15150 1700 15150 1600
Connection ~ 15150 1700
Wire Wire Line
	12450 1900 12450 1800
$Comp
L power:+3.3V #PWR03
U 1 1 5F915ADF
P 15150 1600
F 0 "#PWR03" H 15150 1450 50  0001 C CNN
F 1 "+3.3V" H 15165 1773 50  0000 C CNN
F 2 "" H 15150 1600 50  0001 C CNN
F 3 "" H 15150 1600 50  0001 C CNN
	1    15150 1600
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR04
U 1 1 5F915AE7
P 10000 2700
F 0 "#PWR04" H 10000 2450 50  0001 C CNN
F 1 "GND" H 10005 2527 50  0000 C CNN
F 2 "" H 10000 2700 50  0001 C CNN
F 3 "" H 10000 2700 50  0001 C CNN
	1    10000 2700
	1    0    0    -1  
$EndComp
$Comp
L my-symbols-library:AAT1217 U1
U 1 1 5F915AED
P 13850 1750
F 0 "U1" H 14050 1850 50  0000 C CNN
F 1 "AAT1217" H 14050 1750 50  0000 C CNN
F 2 "Package_TO_SOT_SMD:TSOT-23-6_HandSoldering" H 13850 1500 60  0001 C CNN
F 3 "" H 13250 1150 60  0001 C CNN
	1    13850 1750
	1    0    0    -1  
$EndComp
$Comp
L my-symbols-library:TPS27081A U2
U 1 1 5F915AF3
P 11300 2000
F 0 "U2" H 11275 2487 60  0000 C CNN
F 1 "TPS27081A" H 11275 2381 60  0000 C CNN
F 2 "Package_TO_SOT_SMD:TSOT-23-6_HandSoldering" H 11250 2600 60  0001 C CNN
F 3 "http://www.ti.com/lit/ds/symlink/tps27081a.pdf" H 11275 2381 60  0001 C CNN
	1    11300 2000
	-1   0    0    -1  
$EndComp
Wire Wire Line
	10800 1900 10700 1900
Wire Wire Line
	10700 1900 10700 1800
Connection ~ 10700 1800
Wire Wire Line
	10700 1800 10800 1800
Wire Wire Line
	10800 2050 10700 2050
Wire Wire Line
	10700 2050 10700 2600
Wire Wire Line
	11850 1950 11950 1950
Wire Wire Line
	11950 1950 11950 2100
Wire Wire Line
	11850 2100 11950 2100
Connection ~ 11950 2100
Wire Wire Line
	11950 2100 11950 2600
Wire Wire Line
	10000 2600 10700 2600
Connection ~ 10000 2600
Wire Wire Line
	10000 2600 10000 2700
Wire Wire Line
	11950 2600 10700 2600
Connection ~ 10700 2600
Wire Wire Line
	11850 1800 12450 1800
$Comp
L Device:C C10
U 1 1 5F915B3B
P 1400 9300
F 0 "C10" H 1515 9346 50  0000 L CNN
F 1 "100nF" H 1515 9255 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 1438 9150 50  0001 C CNN
F 3 "~" H 1400 9300 50  0001 C CNN
	1    1400 9300
	1    0    0    -1  
$EndComp
Wire Wire Line
	1400 9450 1400 9550
$Comp
L power:GND #PWR036
U 1 1 5F915B48
P 1400 9550
F 0 "#PWR036" H 1400 9300 50  0001 C CNN
F 1 "GND" H 1405 9377 50  0000 C CNN
F 2 "" H 1400 9550 50  0001 C CNN
F 3 "" H 1400 9550 50  0001 C CNN
	1    1400 9550
	1    0    0    -1  
$EndComp
Wire Wire Line
	1400 9150 1400 9050
$Comp
L power:+3.3V #PWR033
U 1 1 5F915B5D
P 1400 9050
F 0 "#PWR033" H 1400 8900 50  0001 C CNN
F 1 "+3.3V" H 1415 9223 50  0000 C CNN
F 2 "" H 1400 9050 50  0001 C CNN
F 3 "" H 1400 9050 50  0001 C CNN
	1    1400 9050
	1    0    0    -1  
$EndComp
$Comp
L Connector:Conn_ARM_JTAG_SWD_10 J1
U 1 1 5F915BB5
P 9750 9100
F 0 "J1" H 9307 9146 50  0000 R CNN
F 1 "Conn_ARM_JTAG_SWD_10" H 9307 9055 50  0000 R CNN
F 2 "my-footprints-library:Amphenol_FCI_ShroudedSocket_2x05_P1.27mm_Vertical_SMD" H 9750 9100 50  0001 C CNN
F 3 "http://infocenter.arm.com/help/topic/com.arm.doc.ddi0314h/DDI0314H_coresight_components_trm.pdf" V 9400 7850 50  0001 C CNN
	1    9750 9100
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR039
U 1 1 5F915BBB
P 9750 9900
F 0 "#PWR039" H 9750 9650 50  0001 C CNN
F 1 "GND" H 9755 9727 50  0000 C CNN
F 2 "" H 9750 9900 50  0001 C CNN
F 3 "" H 9750 9900 50  0001 C CNN
	1    9750 9900
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR030
U 1 1 5F915BC1
P 9750 8400
F 0 "#PWR030" H 9750 8250 50  0001 C CNN
F 1 "+3.3V" H 9765 8573 50  0000 C CNN
F 2 "" H 9750 8400 50  0001 C CNN
F 3 "" H 9750 8400 50  0001 C CNN
	1    9750 8400
	1    0    0    -1  
$EndComp
NoConn ~ 10400 9200
NoConn ~ 10400 9300
Wire Wire Line
	9750 9900 9750 9800
Wire Wire Line
	9650 9700 9650 9800
Wire Wire Line
	9650 9800 9750 9800
Connection ~ 9750 9800
Wire Wire Line
	9750 9800 9750 9700
Wire Wire Line
	9750 8500 9750 8400
Wire Wire Line
	10250 9200 10400 9200
Wire Wire Line
	10250 9300 10400 9300
Wire Wire Line
	10250 8800 10400 8800
Wire Wire Line
	10250 9000 10400 9000
Wire Wire Line
	10250 9100 10400 9100
Text GLabel 10400 8800 2    50   Input ~ 0
NRST
Text GLabel 10400 9100 2    50   Input ~ 0
SWDIO
Text GLabel 10400 9000 2    50   Input ~ 0
SWCLK
Text GLabel 3200 8350 0    50   Input ~ 0
NRST
Text Notes 10800 1200 0    50   ~ 0
Reverse Polarity Protection
Wire Notes Line
	10550 3100 12100 3100
Wire Notes Line
	12100 3100 12100 1050
Wire Notes Line
	12100 1050 10550 1050
Wire Notes Line
	10550 1050 10550 3100
Wire Notes Line
	12250 3100 15500 3100
Wire Notes Line
	15500 1050 12250 1050
Wire Notes Line
	12250 1050 12250 3100
Wire Notes Line
	15500 1050 15500 3100
Text Notes 13600 1200 0    50   ~ 0
Boost Converter
Wire Notes Line
	1050 10650 1050 7250
Wire Notes Line
	8250 10650 8250 7250
Wire Notes Line
	8250 7250 10950 7250
Wire Notes Line
	10950 7250 10950 10650
Wire Notes Line
	10950 10650 8250 10650
Text Notes 9350 7400 0    50   ~ 0
SWD Connector
$Comp
L Device:C C11
U 1 1 5F915E3E
P 2000 9300
F 0 "C11" H 2115 9346 50  0000 L CNN
F 1 "100nF" H 2115 9255 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 2038 9150 50  0001 C CNN
F 3 "~" H 2000 9300 50  0001 C CNN
	1    2000 9300
	1    0    0    -1  
$EndComp
Wire Wire Line
	2000 9450 2000 9550
$Comp
L power:GND #PWR037
U 1 1 5F915E45
P 2000 9550
F 0 "#PWR037" H 2000 9300 50  0001 C CNN
F 1 "GND" H 2005 9377 50  0000 C CNN
F 2 "" H 2000 9550 50  0001 C CNN
F 3 "" H 2000 9550 50  0001 C CNN
	1    2000 9550
	1    0    0    -1  
$EndComp
Wire Wire Line
	2000 9150 2000 9050
$Comp
L power:+3.3V #PWR034
U 1 1 5F915E4C
P 2000 9050
F 0 "#PWR034" H 2000 8900 50  0001 C CNN
F 1 "+3.3V" H 2015 9223 50  0000 C CNN
F 2 "" H 2000 9050 50  0001 C CNN
F 3 "" H 2000 9050 50  0001 C CNN
	1    2000 9050
	1    0    0    -1  
$EndComp
$Comp
L Device:C C2
U 1 1 5F8C8613
P 1450 2150
F 0 "C2" H 1565 2196 50  0000 L CNN
F 1 "100nF" H 1565 2105 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 1488 2000 50  0001 C CNN
F 3 "~" H 1450 2150 50  0001 C CNN
	1    1450 2150
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR01
U 1 1 5F8DC6AB
P 1450 1600
F 0 "#PWR01" H 1450 1450 50  0001 C CNN
F 1 "+3.3V" H 1465 1773 50  0000 C CNN
F 2 "" H 1450 1600 50  0001 C CNN
F 3 "" H 1450 1600 50  0001 C CNN
	1    1450 1600
	1    0    0    -1  
$EndComp
Wire Wire Line
	1450 1600 1450 1800
Wire Wire Line
	3000 1950 3000 1800
Wire Wire Line
	3000 1800 1450 1800
Connection ~ 1450 1800
Wire Wire Line
	1450 1800 1450 2000
Wire Wire Line
	3400 2150 3500 2150
Text GLabel 3500 2150 2    50   Input ~ 0
SDA
Text GLabel 3500 2250 2    50   Input ~ 0
SCL
Wire Wire Line
	3400 2250 3500 2250
Wire Wire Line
	3000 2700 3000 2550
$Comp
L power:GND #PWR010
U 1 1 5F9157A2
P 3000 2700
F 0 "#PWR010" H 3000 2450 50  0001 C CNN
F 1 "GND" H 3005 2527 50  0000 C CNN
F 2 "" H 3000 2700 50  0001 C CNN
F 3 "" H 3000 2700 50  0001 C CNN
	1    3000 2700
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR09
U 1 1 5F922FA7
P 2500 2700
F 0 "#PWR09" H 2500 2450 50  0001 C CNN
F 1 "GND" H 2505 2527 50  0000 C CNN
F 2 "" H 2500 2700 50  0001 C CNN
F 3 "" H 2500 2700 50  0001 C CNN
	1    2500 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	2600 2350 2500 2350
Wire Wire Line
	2500 2350 2500 2700
Wire Wire Line
	2600 2150 2500 2150
Wire Wire Line
	2500 2150 2500 2350
Connection ~ 2500 2350
$Comp
L power:GND #PWR08
U 1 1 5F930B57
P 1450 2700
F 0 "#PWR08" H 1450 2450 50  0001 C CNN
F 1 "GND" H 1455 2527 50  0000 C CNN
F 2 "" H 1450 2700 50  0001 C CNN
F 3 "" H 1450 2700 50  0001 C CNN
	1    1450 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	1450 2300 1450 2700
Wire Wire Line
	3400 2350 3500 2350
NoConn ~ 3500 2350
Wire Wire Line
	2600 2250 2400 2250
Text GLabel 2400 2250 0    50   Input ~ 0
SHT30_NRST
Wire Wire Line
	14400 1700 15150 1700
Wire Wire Line
	14250 2200 14400 2200
NoConn ~ 14400 2200
Wire Wire Line
	5700 8050 6150 8050
Wire Wire Line
	5700 8050 5550 8050
Connection ~ 5700 8050
Wire Wire Line
	5700 7950 5700 8050
$Comp
L power:+3.3VA #PWR028
U 1 1 5FC71CDC
P 5700 7950
F 0 "#PWR028" H 5700 7800 50  0001 C CNN
F 1 "+3.3VA" H 5715 8123 50  0000 C CNN
F 2 "" H 5700 7950 50  0001 C CNN
F 3 "" H 5700 7950 50  0001 C CNN
	1    5700 7950
	1    0    0    -1  
$EndComp
Wire Wire Line
	6800 7950 6800 8050
$Comp
L power:+3.3V #PWR029
U 1 1 5FC51B03
P 6800 7950
F 0 "#PWR029" H 6800 7800 50  0001 C CNN
F 1 "+3.3V" H 6815 8123 50  0000 C CNN
F 2 "" H 6800 7950 50  0001 C CNN
F 3 "" H 6800 7950 50  0001 C CNN
	1    6800 7950
	1    0    0    -1  
$EndComp
Wire Wire Line
	6350 8050 6800 8050
$Comp
L Device:Ferrite_Bead_Small FB1
U 1 1 5FC319BD
P 6250 8050
F 0 "FB1" V 6013 8050 50  0000 C CNN
F 1 "Ferrite_Bead_Small" V 6104 8050 50  0000 C CNN
F 2 "" V 6180 8050 50  0001 C CNN
F 3 "~" H 6250 8050 50  0001 C CNN
	1    6250 8050
	0    1    1    0   
$EndComp
Text Notes 8000 8650 2    50   ~ 0
TODO: VALUES
Text GLabel 6050 8950 2    50   Input ~ 0
MISO
Text GLabel 6050 9050 2    50   Input ~ 0
MOSI
Text GLabel 6050 8850 2    50   Input ~ 0
SCK
$Comp
L my-symbols-library:STM32L412KB U7
U 1 1 5F915A53
P 5450 9050
F 0 "U7" H 5950 7950 50  0000 C CNN
F 1 "STM32L412KB" H 5950 7850 50  0000 C CNN
F 2 "Package_QFP:LQFP-32_7x7mm_P0.8mm" H 5050 8150 50  0001 R CNN
F 3 "" H 5450 9050 50  0001 C CNN
	1    5450 9050
	1    0    0    -1  
$EndComp
Text Notes 4300 7400 0    50   ~ 0
STM32L4 MCU
Wire Notes Line
	8100 10650 1050 10650
Wire Notes Line
	8100 7250 8100 10650
Wire Notes Line
	1050 7250 8100 7250
NoConn ~ 4850 9050
NoConn ~ 4850 8950
Wire Wire Line
	4950 9050 4850 9050
Wire Wire Line
	4950 8950 4850 8950
Wire Wire Line
	5950 8950 6050 8950
Wire Wire Line
	5950 9150 6050 9150
Wire Wire Line
	4850 9350 4950 9350
Wire Wire Line
	4850 9250 4950 9250
Wire Wire Line
	5950 8750 6050 8750
Connection ~ 3450 8350
Wire Wire Line
	3200 8350 3450 8350
Wire Wire Line
	3450 8350 3450 9400
Wire Wire Line
	3450 8350 4950 8350
Wire Wire Line
	3450 9700 3450 10250
$Comp
L power:GND #PWR040
U 1 1 5F915BE4
P 3450 10250
F 0 "#PWR040" H 3450 10000 50  0001 C CNN
F 1 "GND" H 3455 10077 50  0000 C CNN
F 2 "" H 3450 10250 50  0001 C CNN
F 3 "" H 3450 10250 50  0001 C CNN
	1    3450 10250
	1    0    0    -1  
$EndComp
$Comp
L Device:C C13
U 1 1 5F915BDE
P 3450 9550
F 0 "C13" H 3565 9596 50  0000 L CNN
F 1 "100nF" H 3565 9505 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 3488 9400 50  0001 C CNN
F 3 "~" H 3450 9550 50  0001 C CNN
	1    3450 9550
	1    0    0    -1  
$EndComp
Wire Wire Line
	4950 9450 4850 9450
Wire Wire Line
	6050 9850 5950 9850
Text GLabel 6050 9650 2    50   Input ~ 0
SWDIO
Text GLabel 6050 9750 2    50   Input ~ 0
SWCLK
Wire Wire Line
	5950 9750 6050 9750
Wire Wire Line
	6050 9650 5950 9650
Wire Wire Line
	6050 9550 5950 9550
Wire Wire Line
	5950 9450 6050 9450
Wire Wire Line
	5950 9050 6050 9050
Wire Wire Line
	5950 8850 6050 8850
Wire Wire Line
	6900 9350 7700 9350
Connection ~ 6900 9350
Wire Wire Line
	6900 9000 6900 9350
Wire Wire Line
	6900 8600 6900 8700
Wire Wire Line
	7300 9250 7700 9250
Connection ~ 7300 9250
Wire Wire Line
	7300 9000 7300 9250
Wire Wire Line
	7300 8600 7300 8700
$Comp
L power:+3.3V #PWR032
U 1 1 5F915B9D
P 7300 8600
F 0 "#PWR032" H 7300 8450 50  0001 C CNN
F 1 "+3.3V" H 7315 8773 50  0000 C CNN
F 2 "" H 7300 8600 50  0001 C CNN
F 3 "" H 7300 8600 50  0001 C CNN
	1    7300 8600
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR031
U 1 1 5F915B97
P 6900 8600
F 0 "#PWR031" H 6900 8450 50  0001 C CNN
F 1 "+3.3V" H 6915 8773 50  0000 C CNN
F 2 "" H 6900 8600 50  0001 C CNN
F 3 "" H 6900 8600 50  0001 C CNN
	1    6900 8600
	1    0    0    -1  
$EndComp
$Comp
L Device:R R9
U 1 1 5F915B91
P 7300 8850
F 0 "R9" H 7370 8896 50  0000 L CNN
F 1 "4.7k" H 7370 8805 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 7230 8850 50  0001 C CNN
F 3 "~" H 7300 8850 50  0001 C CNN
	1    7300 8850
	1    0    0    -1  
$EndComp
$Comp
L Device:R R8
U 1 1 5F915B8B
P 6900 8850
F 0 "R8" H 6970 8896 50  0000 L CNN
F 1 "4.7k" H 6970 8805 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 6830 8850 50  0001 C CNN
F 3 "~" H 6900 8850 50  0001 C CNN
	1    6900 8850
	1    0    0    -1  
$EndComp
Wire Wire Line
	6050 8650 5950 8650
Wire Wire Line
	5950 8550 6050 8550
Wire Wire Line
	6050 8450 5950 8450
Wire Wire Line
	5950 8350 6050 8350
Wire Wire Line
	4950 9850 4850 9850
Wire Wire Line
	4950 9750 4850 9750
Wire Wire Line
	4950 9650 4850 9650
Wire Wire Line
	4950 9550 4850 9550
Wire Wire Line
	5950 9250 7300 9250
Wire Wire Line
	5950 9350 6900 9350
Text GLabel 7700 9350 2    50   Input ~ 0
SDA
Text GLabel 7700 9250 2    50   Input ~ 0
SCL
Wire Wire Line
	4050 10250 4050 9700
Wire Wire Line
	4050 8750 4050 9400
Wire Wire Line
	4950 8750 4050 8750
$Comp
L power:GND #PWR041
U 1 1 5F915B32
P 4050 10250
F 0 "#PWR041" H 4050 10000 50  0001 C CNN
F 1 "GND" H 4055 10077 50  0000 C CNN
F 2 "" H 4050 10250 50  0001 C CNN
F 3 "" H 4050 10250 50  0001 C CNN
	1    4050 10250
	1    0    0    -1  
$EndComp
$Comp
L Device:R R10
U 1 1 5F915B2C
P 4050 9550
F 0 "R10" H 4120 9596 50  0000 L CNN
F 1 "10k" H 4120 9505 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 3980 9550 50  0001 C CNN
F 3 "~" H 4050 9550 50  0001 C CNN
	1    4050 9550
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR027
U 1 1 5F915B26
P 5350 7950
F 0 "#PWR027" H 5350 7800 50  0001 C CNN
F 1 "+3.3V" H 5365 8123 50  0000 C CNN
F 2 "" H 5350 7950 50  0001 C CNN
F 3 "" H 5350 7950 50  0001 C CNN
	1    5350 7950
	1    0    0    -1  
$EndComp
Wire Wire Line
	5550 8150 5550 8050
$Comp
L power:GND #PWR042
U 1 1 5F915B1D
P 5350 10250
F 0 "#PWR042" H 5350 10000 50  0001 C CNN
F 1 "GND" H 5355 10077 50  0000 C CNN
F 2 "" H 5350 10250 50  0001 C CNN
F 3 "" H 5350 10250 50  0001 C CNN
	1    5350 10250
	1    0    0    -1  
$EndComp
Wire Wire Line
	5350 8050 5350 7950
Connection ~ 5350 8050
Wire Wire Line
	5450 8050 5350 8050
Wire Wire Line
	5450 8150 5450 8050
Wire Wire Line
	5350 8150 5350 8050
Wire Wire Line
	5350 10150 5350 10250
Connection ~ 5350 10150
Wire Wire Line
	5450 10150 5350 10150
Wire Wire Line
	5450 10050 5450 10150
Wire Wire Line
	5350 10050 5350 10150
$Comp
L Device:C C12
U 1 1 5FCEBC7E
P 2650 9300
F 0 "C12" H 2765 9346 50  0000 L CNN
F 1 "100nF" H 2765 9255 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 2688 9150 50  0001 C CNN
F 3 "~" H 2650 9300 50  0001 C CNN
	1    2650 9300
	1    0    0    -1  
$EndComp
Wire Wire Line
	2650 9450 2650 9550
$Comp
L power:GND #PWR038
U 1 1 5FCEBC85
P 2650 9550
F 0 "#PWR038" H 2650 9300 50  0001 C CNN
F 1 "GND" H 2655 9377 50  0000 C CNN
F 2 "" H 2650 9550 50  0001 C CNN
F 3 "" H 2650 9550 50  0001 C CNN
	1    2650 9550
	1    0    0    -1  
$EndComp
Wire Wire Line
	2650 9150 2650 9050
$Comp
L power:+3.3VA #PWR035
U 1 1 5FCFCA9F
P 2650 9050
F 0 "#PWR035" H 2650 8900 50  0001 C CNN
F 1 "+3.3VA" H 2665 9223 50  0000 C CNN
F 2 "" H 2650 9050 50  0001 C CNN
F 3 "" H 2650 9050 50  0001 C CNN
	1    2650 9050
	1    0    0    -1  
$EndComp
Text Notes 3750 2250 0    50   ~ 0
Address: 0x44
$Comp
L Device:C C3
U 1 1 5F8EA1D1
P 5150 2300
F 0 "C3" H 5265 2346 50  0000 L CNN
F 1 "100nF" H 5265 2255 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 5188 2150 50  0001 C CNN
F 3 "~" H 5150 2300 50  0001 C CNN
	1    5150 2300
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR02
U 1 1 5F8EA1D7
P 5150 1750
F 0 "#PWR02" H 5150 1600 50  0001 C CNN
F 1 "+3.3V" H 5165 1923 50  0000 C CNN
F 2 "" H 5150 1750 50  0001 C CNN
F 3 "" H 5150 1750 50  0001 C CNN
	1    5150 1750
	1    0    0    -1  
$EndComp
Wire Wire Line
	5150 1750 5150 1950
Connection ~ 5150 1950
Wire Wire Line
	5150 1950 5150 2150
$Comp
L power:GND #PWR011
U 1 1 5F8EA1E1
P 5150 2700
F 0 "#PWR011" H 5150 2450 50  0001 C CNN
F 1 "GND" H 5155 2527 50  0000 C CNN
F 2 "" H 5150 2700 50  0001 C CNN
F 3 "" H 5150 2700 50  0001 C CNN
	1    5150 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	5150 2450 5150 2700
$Comp
L power:GND #PWR012
U 1 1 5F8F39F0
P 5700 2700
F 0 "#PWR012" H 5700 2450 50  0001 C CNN
F 1 "GND" H 5705 2527 50  0000 C CNN
F 2 "" H 5700 2700 50  0001 C CNN
F 3 "" H 5700 2700 50  0001 C CNN
	1    5700 2700
	1    0    0    -1  
$EndComp
Wire Wire Line
	5700 2450 5700 2700
Wire Wire Line
	6100 2450 5700 2450
Wire Wire Line
	6100 2350 5700 2350
Wire Wire Line
	5700 2350 5700 1950
Wire Wire Line
	5700 1950 5150 1950
Wire Wire Line
	6800 2350 6900 2350
Text GLabel 6900 2450 2    50   Input ~ 0
SDA
Text GLabel 6900 2350 2    50   Input ~ 0
SCL
Wire Wire Line
	6800 2450 6900 2450
Text Notes 7150 2450 0    50   ~ 0
Address: 0x00
$Comp
L my-symbols-library:TSD305 U4
U 1 1 5F8D5507
P 6450 2400
F 0 "U4" H 6450 2750 50  0000 C CNN
F 1 "TSD305" H 6450 2650 50  0000 C CNN
F 2 "" H 6450 2400 50  0001 C CNN
F 3 "" H 6450 2400 50  0001 C CNN
	1    6450 2400
	-1   0    0    -1  
$EndComp
Wire Wire Line
	10000 1800 10000 2000
Wire Wire Line
	10000 1800 10700 1800
Wire Wire Line
	10000 2400 10000 2600
$Comp
L Device:Battery BT1
U 1 1 5F915A59
P 10000 2200
F 0 "BT1" H 10108 2246 50  0000 L CNN
F 1 "Battery" H 10108 2155 50  0000 L CNN
F 2 "my-footprints-library:BatteryHolder_Keystone_2468_2xAAA_NoSilkscreen" V 10000 2260 50  0001 C CNN
F 3 "~" V 10000 2260 50  0001 C CNN
	1    10000 2200
	1    0    0    -1  
$EndComp
$Comp
L power:GND #PWR018
U 1 1 5F8F70A4
P 7650 5250
F 0 "#PWR018" H 7650 5000 50  0001 C CNN
F 1 "GND" H 7655 5077 50  0000 C CNN
F 2 "" H 7650 5250 50  0001 C CNN
F 3 "" H 7650 5250 50  0001 C CNN
	1    7650 5250
	1    0    0    -1  
$EndComp
$Comp
L power:+3.3V #PWR014
U 1 1 5F8FEDF4
P 7650 4150
F 0 "#PWR014" H 7650 4000 50  0001 C CNN
F 1 "+3.3V" H 7665 4323 50  0000 C CNN
F 2 "" H 7650 4150 50  0001 C CNN
F 3 "" H 7650 4150 50  0001 C CNN
	1    7650 4150
	1    0    0    -1  
$EndComp
Wire Wire Line
	7650 5150 7650 5250
Wire Wire Line
	7650 4150 7650 4350
$Comp
L Amplifier_Operational:TSV911RILT U5
U 1 1 5F8E681D
P 7750 4850
F 0 "U5" H 7900 4700 50  0000 L CNN
F 1 "TSV911RILT" H 7750 4600 50  0000 L CNN
F 2 "Package_TO_SOT_SMD:SOT-23-5" H 7750 4850 50  0001 C CNN
F 3 "www.st.com/resource/en/datasheet/tsv911.pdf" H 7750 5050 50  0001 C CNN
	1    7750 4850
	1    0    0    -1  
$EndComp
$Comp
L Device:R R7
U 1 1 5F97901A
P 8100 5650
F 0 "R7" H 8170 5696 50  0000 L CNN
F 1 "1M" H 8170 5605 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 8030 5650 50  0001 C CNN
F 3 "~" H 8100 5650 50  0001 C CNN
	1    8100 5650
	0    1    1    0   
$EndComp
Wire Wire Line
	8050 4850 8400 4850
Wire Wire Line
	8400 4850 8400 5650
Wire Wire Line
	8400 5650 8250 5650
$Comp
L Device:C C9
U 1 1 5F9BD83E
P 8100 6100
F 0 "C9" H 8215 6146 50  0000 L CNN
F 1 "3pF" H 8215 6055 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 8138 5950 50  0001 C CNN
F 3 "~" H 8100 6100 50  0001 C CNN
	1    8100 6100
	0    1    1    0   
$EndComp
Wire Wire Line
	8250 6100 8400 6100
Wire Wire Line
	8400 6100 8400 5650
Connection ~ 8400 5650
Wire Wire Line
	7950 6100 7800 6100
Wire Wire Line
	7800 6100 7800 5650
Connection ~ 7800 5650
$Comp
L Device:C C5
U 1 1 5FA4C241
P 10050 4850
F 0 "C5" H 10165 4896 50  0000 L CNN
F 1 "100nF" H 10165 4805 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 10088 4700 50  0001 C CNN
F 3 "~" H 10050 4850 50  0001 C CNN
	1    10050 4850
	1    0    0    -1  
$EndComp
Wire Wire Line
	7650 4350 10050 4350
Wire Wire Line
	10050 4350 10050 4700
Connection ~ 7650 4350
Wire Wire Line
	7650 4350 7650 4550
$Comp
L power:GND #PWR026
U 1 1 5FA712C7
P 10050 6250
F 0 "#PWR026" H 10050 6000 50  0001 C CNN
F 1 "GND" H 10055 6077 50  0000 C CNN
F 2 "" H 10050 6250 50  0001 C CNN
F 3 "" H 10050 6250 50  0001 C CNN
	1    10050 6250
	1    0    0    -1  
$EndComp
Wire Wire Line
	10050 6250 10050 5000
Wire Wire Line
	8400 4850 8550 4850
Connection ~ 8400 4850
Text GLabel 9150 4850 2    50   Input ~ 0
PHOTO_SENSE
Wire Wire Line
	6000 4750 6000 4900
Wire Wire Line
	6700 4750 6000 4750
Wire Wire Line
	6000 6250 6000 5200
$Comp
L power:GND #PWR022
U 1 1 5FA55252
P 6000 6250
F 0 "#PWR022" H 6000 6000 50  0001 C CNN
F 1 "GND" H 6005 6077 50  0000 C CNN
F 2 "" H 6000 6250 50  0001 C CNN
F 3 "" H 6000 6250 50  0001 C CNN
	1    6000 6250
	1    0    0    -1  
$EndComp
$Comp
L Device:C C6
U 1 1 5FA209C1
P 6000 5050
F 0 "C6" H 6115 5096 50  0000 L CNN
F 1 "100nF" H 6115 5005 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 6038 4900 50  0001 C CNN
F 3 "~" H 6000 5050 50  0001 C CNN
	1    6000 5050
	1    0    0    -1  
$EndComp
Wire Wire Line
	6700 6250 6700 5200
$Comp
L power:GND #PWR023
U 1 1 5FA0334F
P 6700 6250
F 0 "#PWR023" H 6700 6000 50  0001 C CNN
F 1 "GND" H 6705 6077 50  0000 C CNN
F 2 "" H 6700 6250 50  0001 C CNN
F 3 "" H 6700 6250 50  0001 C CNN
	1    6700 6250
	1    0    0    -1  
$EndComp
Wire Wire Line
	6700 4150 6700 4300
$Comp
L power:+3.3V #PWR013
U 1 1 5F9FA1A6
P 6700 4150
F 0 "#PWR013" H 6700 4000 50  0001 C CNN
F 1 "+3.3V" H 6715 4323 50  0000 C CNN
F 2 "" H 6700 4150 50  0001 C CNN
F 3 "" H 6700 4150 50  0001 C CNN
	1    6700 4150
	1    0    0    -1  
$EndComp
$Comp
L Device:R R2
U 1 1 5F9F101D
P 6700 4450
F 0 "R2" H 6770 4496 50  0000 L CNN
F 1 "330k" H 6770 4405 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 6630 4450 50  0001 C CNN
F 3 "~" H 6700 4450 50  0001 C CNN
	1    6700 4450
	-1   0    0    1   
$EndComp
$Comp
L Device:R R6
U 1 1 5F9E865B
P 6700 5050
F 0 "R6" H 6770 5096 50  0000 L CNN
F 1 "10k" H 6770 5005 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 6630 5050 50  0001 C CNN
F 3 "~" H 6700 5050 50  0001 C CNN
	1    6700 5050
	-1   0    0    1   
$EndComp
Connection ~ 6700 4750
Wire Wire Line
	6700 4750 6700 4900
Wire Wire Line
	6700 4750 6700 4600
Wire Wire Line
	7450 4750 6700 4750
Wire Wire Line
	7950 5650 7800 5650
Wire Wire Line
	7800 5650 7100 5650
Wire Wire Line
	7100 5650 7100 5800
Connection ~ 7100 5650
Wire Wire Line
	7100 6250 7100 6100
Wire Wire Line
	7100 4950 7100 5650
Wire Wire Line
	7450 4950 7100 4950
$Comp
L power:GND #PWR024
U 1 1 5F942D81
P 7100 6250
F 0 "#PWR024" H 7100 6000 50  0001 C CNN
F 1 "GND" H 7105 6077 50  0000 C CNN
F 2 "" H 7100 6250 50  0001 C CNN
F 3 "" H 7100 6250 50  0001 C CNN
	1    7100 6250
	1    0    0    -1  
$EndComp
$Comp
L Device:D_Photo D1
U 1 1 5F8E4B55
P 7100 6000
F 0 "D1" V 7004 6158 50  0000 L CNN
F 1 "D_Photo" V 7095 6158 50  0000 L CNN
F 2 "" H 7050 6000 50  0001 C CNN
F 3 "~" H 7050 6000 50  0001 C CNN
	1    7100 6000
	0    1    1    0   
$EndComp
$Comp
L Device:R R5
U 1 1 5FAFD453
P 8700 4850
F 0 "R5" H 8770 4896 50  0000 L CNN
F 1 "10k" H 8770 4805 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 8630 4850 50  0001 C CNN
F 3 "~" H 8700 4850 50  0001 C CNN
	1    8700 4850
	0    1    1    0   
$EndComp
$Comp
L Device:C C7
U 1 1 5FAFD459
P 9000 5300
F 0 "C7" H 9115 5346 50  0000 L CNN
F 1 "100nF" H 9115 5255 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 9038 5150 50  0001 C CNN
F 3 "~" H 9000 5300 50  0001 C CNN
	1    9000 5300
	1    0    0    -1  
$EndComp
Wire Wire Line
	9000 6250 9000 5450
$Comp
L power:GND #PWR025
U 1 1 5FB118A1
P 9000 6250
F 0 "#PWR025" H 9000 6000 50  0001 C CNN
F 1 "GND" H 9005 6077 50  0000 C CNN
F 2 "" H 9000 6250 50  0001 C CNN
F 3 "" H 9000 6250 50  0001 C CNN
	1    9000 6250
	1    0    0    -1  
$EndComp
Wire Wire Line
	8850 4850 9000 4850
Wire Wire Line
	9000 4850 9000 5150
Wire Wire Line
	9150 4850 9000 4850
Connection ~ 9000 4850
Text Notes 5300 4700 0    50   ~ 0
Offset: 20/(330+20)*3.3V=188mV
Text Notes 10750 4350 0    50   ~ 0
1lux ~~ 1.46e-7w/cm2
Text Notes 10750 4900 0    50   ~ 0
Sunlight: 100000lux\nDaylight: 10000lux\nCloudy: 1000lux\nVery dark day: 100lux\nTwilight: 10lux\nNight: <1lux
Wire Notes Line
	4750 1050 7900 1050
Wire Notes Line
	7900 1050 7900 3100
Wire Notes Line
	7900 3100 4750 3100
Wire Notes Line
	4750 3100 4750 1050
Text Notes 5700 1200 0    50   ~ 0
Infrared Temperature Sensor (optional)
Wire Notes Line
	1050 3100 1050 1050
Wire Notes Line
	4600 1050 4600 3100
Wire Notes Line
	1050 1050 4600 1050
Wire Notes Line
	1050 3100 4600 3100
Text Notes 2100 1200 0    50   ~ 0
Temperature / Humidity Sensor
Text Notes 6500 9900 0    50   ~ 0
TODO: CONNECTIONS\n
Wire Notes Line
	5000 3700 5000 6650
Wire Notes Line
	1050 6650 1050 3700
Text Notes 2650 3850 0    50   ~ 0
LoRA Module
Wire Notes Line
	12050 3700 12050 6650
Wire Notes Line
	4850 6650 4850 3700
Wire Notes Line
	4850 3700 1050 3700
Wire Notes Line
	1050 6650 4850 6650
Wire Notes Line
	5000 6650 12050 6650
Wire Notes Line
	12050 3700 5000 3700
Text Notes 8300 3850 0    50   ~ 0
Light Sensor
$Comp
L power:+3.3V #PWR017
U 1 1 5F8F769A
P 4350 4450
F 0 "#PWR017" H 4350 4300 50  0001 C CNN
F 1 "+3.3V" H 4365 4623 50  0000 C CNN
F 2 "" H 4350 4450 50  0001 C CNN
F 3 "" H 4350 4450 50  0001 C CNN
	1    4350 4450
	1    0    0    -1  
$EndComp
Wire Wire Line
	4350 5950 4350 6250
$Comp
L power:GND #PWR021
U 1 1 5F99C085
P 4350 6250
F 0 "#PWR021" H 4350 6000 50  0001 C CNN
F 1 "GND" H 4355 6077 50  0000 C CNN
F 2 "" H 4350 6250 50  0001 C CNN
F 3 "" H 4350 6250 50  0001 C CNN
	1    4350 6250
	1    0    0    -1  
$EndComp
$Comp
L Device:C C8
U 1 1 5F98D147
P 4350 5800
F 0 "C8" H 4465 5846 50  0000 L CNN
F 1 "100nF" H 4465 5755 50  0000 L CNN
F 2 "Capacitor_SMD:C_0805_2012Metric_Pad1.15x1.40mm_HandSolder" H 4388 5650 50  0001 C CNN
F 3 "~" H 4350 5800 50  0001 C CNN
	1    4350 5800
	1    0    0    -1  
$EndComp
Wire Wire Line
	3850 5850 3850 6250
$Comp
L power:GND #PWR020
U 1 1 5F9971A4
P 3850 6250
F 0 "#PWR020" H 3850 6000 50  0001 C CNN
F 1 "GND" H 3855 6077 50  0000 C CNN
F 2 "" H 3850 6250 50  0001 C CNN
F 3 "" H 3850 6250 50  0001 C CNN
	1    3850 6250
	1    0    0    -1  
$EndComp
Text Notes 3800 6150 2    50   ~ 0
TODO: ANTENNA
$Comp
L power:GND #PWR019
U 1 1 5F992340
P 2550 6250
F 0 "#PWR019" H 2550 6000 50  0001 C CNN
F 1 "GND" H 2555 6077 50  0000 C CNN
F 2 "" H 2550 6250 50  0001 C CNN
F 3 "" H 2550 6250 50  0001 C CNN
	1    2550 6250
	1    0    0    -1  
$EndComp
Text GLabel 1950 5750 0    50   Input ~ 0
RFM95W_RST
Text GLabel 1950 5650 0    50   Input ~ 0
RFM95W_NSS
Text GLabel 1950 5550 0    50   Input ~ 0
SCK
Text GLabel 1950 5450 0    50   Input ~ 0
MOSI
Text GLabel 1950 5350 0    50   Input ~ 0
MISO
Wire Wire Line
	2450 4400 2450 4550
$Comp
L power:+3.3V #PWR016
U 1 1 5FA91C0B
P 2450 4400
F 0 "#PWR016" H 2450 4250 50  0001 C CNN
F 1 "+3.3V" H 2465 4573 50  0000 C CNN
F 2 "" H 2450 4400 50  0001 C CNN
F 3 "" H 2450 4400 50  0001 C CNN
	1    2450 4400
	1    0    0    -1  
$EndComp
Wire Wire Line
	2050 4400 2050 4550
$Comp
L power:+3.3V #PWR015
U 1 1 5FA8AEAD
P 2050 4400
F 0 "#PWR015" H 2050 4250 50  0001 C CNN
F 1 "+3.3V" H 2065 4573 50  0000 C CNN
F 2 "" H 2050 4400 50  0001 C CNN
F 3 "" H 2050 4400 50  0001 C CNN
	1    2050 4400
	1    0    0    -1  
$EndComp
Wire Wire Line
	2050 4850 2050 5750
Wire Wire Line
	2450 4850 2450 5650
$Comp
L Device:R R3
U 1 1 5FA530B6
P 2050 4700
F 0 "R3" H 2120 4746 50  0000 L CNN
F 1 "100k" H 2120 4655 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 1980 4700 50  0001 C CNN
F 3 "~" H 2050 4700 50  0001 C CNN
	1    2050 4700
	1    0    0    -1  
$EndComp
$Comp
L Device:R R4
U 1 1 5FA4C619
P 2450 4700
F 0 "R4" H 2520 4746 50  0000 L CNN
F 1 "100k" H 2520 4655 50  0000 L CNN
F 2 "Resistor_SMD:R_0805_2012Metric_Pad1.15x1.40mm_HandSolder" V 2380 4700 50  0001 C CNN
F 3 "~" H 2450 4700 50  0001 C CNN
	1    2450 4700
	1    0    0    -1  
$EndComp
Wire Wire Line
	2050 5750 1950 5750
Wire Wire Line
	2450 5650 1950 5650
Wire Wire Line
	3550 5850 3850 5850
NoConn ~ 3650 5250
NoConn ~ 3650 5350
NoConn ~ 3650 5650
NoConn ~ 3650 5750
Wire Wire Line
	3550 5750 3650 5750
Wire Wire Line
	3550 5650 3650 5650
Wire Wire Line
	3550 5450 3650 5450
Wire Wire Line
	3550 5350 3650 5350
Wire Wire Line
	3550 5250 3650 5250
Wire Wire Line
	4350 4450 4350 5550
Wire Wire Line
	4350 5550 4350 5650
Connection ~ 4350 5550
Wire Wire Line
	3550 5550 4350 5550
Wire Wire Line
	1950 5450 2750 5450
Wire Wire Line
	1950 5350 2750 5350
Wire Wire Line
	1950 5550 2750 5550
Connection ~ 2050 5750
Wire Wire Line
	2050 5750 2750 5750
Connection ~ 2450 5650
Wire Wire Line
	2450 5650 2750 5650
NoConn ~ 2650 5850
Wire Wire Line
	2750 5850 2650 5850
Wire Wire Line
	2550 5950 2550 6250
Wire Wire Line
	2750 5950 2550 5950
Connection ~ 2550 5950
Wire Wire Line
	2550 5250 2550 5950
Wire Wire Line
	2750 5250 2550 5250
$Comp
L my-symbols-library:RFM95W U6
U 1 1 5F98AF14
P 3150 5600
F 0 "U6" H 3150 6215 50  0000 C CNN
F 1 "RFM95W" H 3150 6124 50  0000 C CNN
F 2 "" H 3150 5100 50  0001 C CNN
F 3 "" H 3150 5100 50  0001 C CNN
	1    3150 5600
	1    0    0    -1  
$EndComp
Text GLabel 3650 5450 2    50   Input ~ 0
RFM95_IRQ
$Comp
L Sensor_Humidity:SHT31-DIS U?
U 1 1 5FAE73AC
P 3000 2250
F 0 "U?" H 3300 2650 50  0000 C CNN
F 1 "SHT31-DIS" H 3300 2550 50  0000 C CNN
F 2 "Sensor_Humidity:Sensirion_DFN-8-1EP_2.5x2.5mm_P0.5mm_EP1.1x1.7mm" H 3000 2300 50  0001 C CNN
F 3 "https://www.sensirion.com/fileadmin/user_upload/customers/sensirion/Dokumente/2_Humidity_Sensors/Datasheets/Sensirion_Humidity_Sensors_SHT3x_Datasheet_digital.pdf" H 3000 2300 50  0001 C CNN
	1    3000 2250
	1    0    0    -1  
$EndComp
$EndSCHEMATC
