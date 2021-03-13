package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sensors")
public class Sensor
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String ttnId;

	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;
}
