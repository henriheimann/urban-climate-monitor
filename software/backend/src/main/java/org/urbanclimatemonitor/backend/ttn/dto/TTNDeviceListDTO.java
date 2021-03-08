package org.urbanclimatemonitor.backend.ttn.dto;

import lombok.Data;

import java.util.List;

@Data
public class TTNDeviceListDTO
{
	private List<TTNDeviceDTO> devices;
}
