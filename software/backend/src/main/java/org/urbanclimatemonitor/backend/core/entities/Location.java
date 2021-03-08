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
	private String id;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location")
	@ToString.Exclude
	private List<Device> devices;
}
