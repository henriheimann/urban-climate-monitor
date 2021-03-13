package org.urbanclimatemonitor.backend.core.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "locations")
public class Location
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	@ToString.Exclude
	private List<Sensor> sensors;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icon_id", referencedColumnName = "id")
	private Upload icon;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "model3d_id", referencedColumnName = "id")
	private Upload model3d;
}
