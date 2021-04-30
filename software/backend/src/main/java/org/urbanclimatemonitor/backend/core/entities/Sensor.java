package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sensors")
public class Sensor
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String ttnId;

	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	private Float locationPositionX;
	private Float locationPositionY;
	private Float locationPositionZ;

	private Float locationRotationX;
	private Float locationRotationY;
	private Float locationRotationZ;

	public Sensor(String name, Location location)
	{
		this.name = name;
		this.location = location;
	}
}