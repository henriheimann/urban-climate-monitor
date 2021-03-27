package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	private List<Sensor> sensors;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "icon_id", referencedColumnName = "id")
	private Upload icon;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "model3d_id", referencedColumnName = "id")
	private Upload model3d;

	public Location(String name, Upload icon, Upload model3d)
	{
		this.name = name;
		this.sensors = new ArrayList<>();
		this.icon = icon;
		this.model3d = model3d;
	}
}
