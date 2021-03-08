package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "devices")
public class Device
{
	@Id
	private String id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;
}
